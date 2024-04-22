package flaskspring.demo.departure.service;

import com.amazonaws.services.s3.transfer.Copy;
import com.querydsl.core.Tuple;
import flaskspring.demo.departure.domain.DeparturePoint;
import flaskspring.demo.departure.dto.res.ResLocation;
import flaskspring.demo.departure.dto.res.ResSchedule;
import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.departure.repository.DepartureRepository;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.register.repository.PlaceRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {

    private static final int INF = 999999999;

    private final MemberRepository memberRepository;
    private final DepartureRepository departureRepository;
    private final PlaceRegisterRepository placeRegisterRepository;

    public BaseResponse<ResSchedule> getSchedule(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        // 사용자의 현재 위치를 출발지로 설정
        Optional<DeparturePoint> departure = departureRepository.findFirstByMemberOrderByCreatedTimeDesc(member);
        List<ResSchedulePlace> schedulePlaces = getSchedulePlaces(member); // 등록일자 순

        // locations 리스트를 생성하고 정렬하기
        List<ResLocation> locations = getLocations(schedulePlaces);

        double fullShortestDistance = 0;
        ArrayList<Integer> paths = new ArrayList<>();
        List<Double> distanceFromPrev = new ArrayList<>();

        if (departure.isPresent()) {
            DeparturePoint departurePoint = departure.get();
            locations.add(0, new ResLocation(departurePoint.getLocation())); // 출발지를 리스트의 맨 앞에 추가하여 출발 도시로 설정

            double[][] distanceMatrix = initDistanceMatrix(locations);
            double[][] DPMatrix = initDPMatrix(locations);

            fullShortestDistance = calculateShortestSchedule(distanceMatrix, DPMatrix);
            findOptimalPath(0, 1, locations.size(), distanceMatrix, DPMatrix, paths, distanceFromPrev);

            List<ResSchedulePlace> sortedSchedulePlace = getSortedSchedulePlace(schedulePlaces, paths);
            updateDistanceFromPrev(sortedSchedulePlace, distanceFromPrev);

            return new BaseResponse<>(BaseResponseCode.CREATED, new ResSchedule(departurePoint, fullShortestDistance, sortedSchedulePlace)); //출발지 있을 경우 201
        }
        return new BaseResponse<>(BaseResponseCode.OK, new ResSchedule((DeparturePoint) null, fullShortestDistance, schedulePlaces)); //없는 경우 200
    }

    private void updateDistanceFromPrev(List<ResSchedulePlace> sortedSchedulePlaces, List<Double> distanceFromPrev) {
        for (int i = 0; i < sortedSchedulePlaces.size(); i++) {
            sortedSchedulePlaces.get(i).setDistanceFromPrevious(distanceFromPrev.get(i));
        }
    }

    private List<ResSchedulePlace> getSortedSchedulePlace(List<ResSchedulePlace> schedulePlaces, List<Integer> paths) {
        List<ResSchedulePlace> sortedSchedulePlaces = new ArrayList<>();
        for (int i = 1; i <= schedulePlaces.size(); i++) {
            sortedSchedulePlaces.add(schedulePlaces.get(paths.get(i) - 1));
        }
        return sortedSchedulePlaces;
    }

    private List<ResLocation> getLocations(List<ResSchedulePlace> schedulePlaces) {
        List<ResLocation> locations = new ArrayList<>();
        for (ResSchedulePlace schedulePlace : schedulePlaces) {
            locations.add(schedulePlace.getLocation());
        }
        return locations;
    }

    private List<ResSchedulePlace> getSchedulePlaces(Member member) {
        List<Tuple> registeredPlaces = placeRegisterRepository.findScheduleByMember(member);
        return registeredPlaces.stream().map(ResSchedulePlace::new).toList();
    }

    private void findOptimalPath(int startCityIndex, int visited, int totalCities,
                                 double[][] distanceMatrix, double[][] dpMatrix, List<Integer> path, List<Double> distanceFromPrev) {
        int currentCityIndex = startCityIndex;
        path.add(currentCityIndex);

        if (visited == (1 << totalCities) - 1) {
            return;
        }

        double[] nextCostAndCity = {INF, 0};
        double distance = INF;
        for (int nextCityIndex = 0; nextCityIndex < totalCities; nextCityIndex++) {
            if ((visited & (1 << nextCityIndex)) == 0) { // 방문 안 한 경우
                double newCost = distanceMatrix[currentCityIndex][nextCityIndex] + dpMatrix[nextCityIndex][visited | (1 << nextCityIndex)];
                if (newCost < nextCostAndCity[0]) {
                    nextCostAndCity[0] = newCost;
                    nextCostAndCity[1] = nextCityIndex;
                    distance = distanceMatrix[currentCityIndex][nextCityIndex];
                }
            }
        }
        distanceFromPrev.add(distance);
        findOptimalPath((int) nextCostAndCity[1], visited | (1 << (int) nextCostAndCity[1]), totalCities, distanceMatrix, dpMatrix, path, distanceFromPrev);
    }

    private double[][] initDistanceMatrix(List<ResLocation> locations) {
        int N = locations.size();
        double[][] W = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                W[i][j] = calculateDistance(locations.get(i), locations.get(j));
            }
        }
        return W;
    }

    private double[][] initDPMatrix(List<ResLocation> locations) {
        int N = locations.size();
        System.out.println("N = " + N);
        double[][] dp = new double[N][1 << N];
        for (double[] row : dp) {
            Arrays.fill(row, INF);
        }
        return dp;
    }

    private double calculateShortestSchedule(double[][] distanceMatrix, double[][] dpMatrix) {
        int totalCities = distanceMatrix[0].length;
        int startCity = 0; // 출발 도시는 0번
        return calculateMinimumCost(startCity, 1, totalCities, distanceMatrix, dpMatrix);
    }

    private double calculateMinimumCost(int currentCityIndex, int visited, int totalCities,
                                        double[][] distanceMatrix, double[][] dpMatrix) {

        if (visited == (1 << totalCities) - 1) {
            dpMatrix[currentCityIndex][visited] = 0;
            return 0;
        }

        if (dpMatrix[currentCityIndex][visited] != INF) {
            return dpMatrix[currentCityIndex][visited];
        }


        for (int nextCityIndex = 0; nextCityIndex < totalCities; nextCityIndex++) {
            if ((visited & (1 << nextCityIndex)) == 0 && distanceMatrix[currentCityIndex][nextCityIndex] != 0) {
                double newCost = calculateMinimumCost(nextCityIndex, visited | (1 << nextCityIndex), totalCities, //Or로 방문처리
                        distanceMatrix, dpMatrix) +
                        distanceMatrix[currentCityIndex][nextCityIndex];
                dpMatrix[currentCityIndex][visited] = Math.min(newCost, dpMatrix[currentCityIndex][visited]);
            }
        }

        return dpMatrix[currentCityIndex][visited];
    }

    private static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    private double calculateDistance(ResLocation place1, ResLocation place2) {

        double startLat = Double.parseDouble(place1.getMapY());
        double startLon = Double.parseDouble(place1.getMapX());
        double endLat = Double.parseDouble(place2.getMapY());
        double endLon = Double.parseDouble(place2.getMapX());

        double dLat = Math.toRadians(endLat - startLat);
        double dLon = Math.toRadians(endLon - startLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c;

    }
}

package flaskspring.demo.tag.service;

import flaskspring.demo.like.service.PlaceLikeService;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.Res.GeneralSignUpRes;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.dto.res.ResPlaceWithSim;
import flaskspring.demo.recommend.dto.res.ImgRecommendationDto;
import flaskspring.demo.tag.dto.res.ResTag;
import flaskspring.demo.place.dto.res.ResPlace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
class FeedServiceTest {

    @Autowired
    TagService tagService;

    @Autowired
    MemberService memberService;

    @Autowired
    FeedService feedService;

    @Autowired
    PlaceLikeService placeLikeService;

    Random random = new Random();

    @Test
    void getRecommendFeedTestByImage() {
        Long savedMemberId = 1L;
        List<ImgRecommendationDto> recommendationDto = getRecommendationDto();
        List<ResPlaceWithSim> recommendFeed = feedService.getRecommendFeed(1L, recommendationDto);
        printResPlaces(recommendFeed);
    }

    private static void printResPlaces(List<ResPlaceWithSim> places) {
        System.out.println("Recommended Places:");
        for (ResPlaceWithSim place : places) {
            System.out.println("Place ID: " + place.getPlaceId());
            System.out.println("Place Name: " + place.getPlaceName());
            System.out.println("Place Image URL: " + place.getPlaceImgUrl());
            System.out.println("Tags: " + place.getTags().stream().map(ResTag::getTagName).toList());
            System.out.println("Likes Count: " + place.getLikesCount());
            System.out.println("Register Count: " + place.getRegisterCount());
            System.out.println("Is Liked: " + place.getIsLiked());
            System.out.println("Is Registered: " + place.getIsRegistered());
            System.out.println("-------------------------------------");
        }
    }

    private List<ImgRecommendationDto> getRecommendationDto() {

        List<String> placeNames = List.of(
                "중앙시장",
                "CGV동두천",
                "동두천큰시장",
                "양키시장",
                "영재파3골프랜드",
                "강천보한강문화관",
                "신륵사관광지",
                "은아목장",
                "연천당포성",
                "연천파크골프장",
                "고대산자연휴양림",
                "한탄강관광지",
                "연천경순왕릉",
                "신탄리역",
                "동막골유황천",
                "열쇠전망대",
                "상천역",
                "산장관광지",
                "청심평화월드센터",
                "가평사계절썰매장",
                "음악역1939"
        );
        return generateRecommendationDtos(placeNames);
    }

    private static List<ImgRecommendationDto> generateRecommendationDtos(List<String> placeNames) {
        List<ImgRecommendationDto> recommendationDtos = new ArrayList<>();
        Random random = new Random();

        for (String name : placeNames) {
            double similarity = random.nextDouble(); // 0부터 1 사이의 무작위한 유사도를 생성합니다.
            ImgRecommendationDto dto = new ImgRecommendationDto(name, similarity);
            recommendationDtos.add(dto);
        }

        return recommendationDtos;
    }

    @Test
    void getRecommendFeedTest() {
        Long savedMemberId = 1L;
        createMemberTags(savedMemberId, List.of(1L, 3L, 5L, 7L));
        List<ResPlace> recommendFeed = feedService.getRecommendFeed(savedMemberId, "recommend");
        recommendFeed.forEach(System.out::println);

    }

    private void createMemberTags(Long memberId, List<Long> TagIndexes) {
        tagService.saveMemberTags(memberId, TagIndexes);
    }

    private Long createMember() {
        GeneralSignUpReq member = GeneralSignUpReq.builder()
                .account("testAccount")
                .password("testPassword")
                .name("testName")
                .build();

        GeneralSignUpRes generalSignUpRes = memberService.generalSignUp(member);
        return generalSignUpRes.getMemberId();
    }
}
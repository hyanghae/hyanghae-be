package flaskspring.demo.tag.service;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.Res.GeneralSignUpRes;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.tag.dto.res.ResCategoryTag;
import flaskspring.demo.tag.dto.res.ResRegisteredTag;
import flaskspring.demo.tag.dto.res.ResTag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    TagService tagService;

    @Autowired
    MemberService memberService;

    @Test
    void printSql() {
        Set<Integer> selectedPlace = new HashSet<>();
        // SQL 문장 출력
        for (int memberId = 1; memberId <= 3; memberId++) {
            selectedPlace.clear();
            for (int j = 0; j < 10; j++) {

                int randomPlaceId;
                do {
                    randomPlaceId = ThreadLocalRandom.current().nextInt(1, 741);
                } while (selectedPlace.contains(randomPlaceId)); // 이미 선택된 태그와 겹치지 않을 때까지 다시 뽑습니다.
                selectedPlace.add(randomPlaceId); // 선택된 태그를 기억합니다.
                System.out.println("insert into place_register(member_id, place_id, created_time) values(" + memberId + ", " + randomPlaceId + ", now());");
                //System.out.println("insert into place_like(member_id, place_id) values(" + memberId + ", "+randomPlaceId+");");
                // System.out.println("INSERT INTO place_tag_log (place_id, tag_id) VALUES (" + placeId + ", " + randomTagId + ");");

            }
        }
    }

    @Test
    void printSql2() {
        // 인기 여행지 개수
        int popularPlaceCount = 222;
        // 태그 개수
        int tagCount = 24;
        // 한 인기 여행지 당 매핑할 태그 수
        int tagsPerPopularPlace = 4;

        // 인기 여행지마다 선택된 태그를 기억할 Map
        Map<Integer, Set<Integer>> selectedTagsMap = new HashMap<>();

        // 인기 여행지 개수만큼 반복
        for (int i = 1; i <= popularPlaceCount; i++) {
            // 현재 인기 여행지에 대해 선택된 태그를 초기화
            Set<Integer> selectedTags = new HashSet<>();

            // 현재 인기 여행지에 대해 태그 개수만큼 반복하여 태그를 선택
            for (int j = 0; j < tagsPerPopularPlace; j++) {
                int randomTagId;
                // 이미 선택된 태그와 겹치지 않을 때까지 다시 뽑습니다.
                do {
                    randomTagId = ThreadLocalRandom.current().nextInt(1, tagCount + 1);
                } while (selectedTags.contains(randomTagId));
                // 선택된 태그를 기억합니다.
                selectedTags.add(randomTagId);
                // 현재 선택된 태그와 점수를 현재 인기 여행지에 대해 출력합니다.
                int score = ThreadLocalRandom.current().nextInt(0, 11);
                System.out.println("INSERT INTO famous_place_tag_log (famous_place_id, tag_id, tag_score) VALUES (" + i + ", " + randomTagId + ", " + score + ");");
            }

            // 선택된 태그를 맵에 추가합니다.
            selectedTagsMap.put(i, selectedTags);
        }
    }


    @Test
    void printSql3() {
        // 여행지 개수
        int placeCount = 740;
        // 태그 개수
        int tagCount = 24;
        // 한 여행지 당 매핑할 태그 수
        int tagsPerPlace = 4;

        // 여행지마다 선택된 태그를 기억할 Map
        Map<Integer, Set<Integer>> selectedTagsMap = new HashMap<>();

        // 여행지 개수만큼 반복
        for (int i = 1; i <= placeCount; i++) {
            // 현재 여행지에 대해 선택된 태그를 초기화
            Set<Integer> selectedTags = new HashSet<>();

            // 현재 여행지에 대해 태그 개수만큼 반복하여 태그를 선택
            for (int j = 0; j < tagsPerPlace; j++) {
                int randomTagId;
                // 이미 선택된 태그와 겹치지 않을 때까지 다시 뽑습니다.
                do {
                    randomTagId = ThreadLocalRandom.current().nextInt(1, tagCount + 1);
                } while (selectedTags.contains(randomTagId));
                // 선택된 태그를 기억합니다.
                selectedTags.add(randomTagId);

                // 현재 선택된 태그와 점수를 현재 여행지에 대해 출력합니다.
                int score = ThreadLocalRandom.current().nextInt(0, 11);
                System.out.println("INSERT INTO place_tag_log (place_id, tag_id, tag_score) VALUES (" + i + ", " + randomTagId + ", " + score + ");");
            }

            // 선택된 태그를 맵에 추가합니다.
            selectedTagsMap.put(i, selectedTags);
        }
    }


    @Test
    void getAllTagTest() {
        // 테스트에서 getAllTag() 메서드를 호출하여 모든 태그를 가져옵니다.
        List<ResCategoryTag> allTags = tagService.getAllTag();

        // 가져온 태그가 null이 아닌지 확인합니다.
        assertThat(allTags).isNotNull();

        // 가져온 태그의 개수가 예상한 개수와 일치하는지 확인합니다.
        assertThat(allTags.size()).isEqualTo(6);

        // 각 카테고리와 해당 카테고리에 속하는 태그를 확인합니다.
        for (ResCategoryTag categoryTag : allTags) {
            System.out.println("Category: " + categoryTag.getCategoryName());
            List<ResTag> tags = categoryTag.getTags();
            assertThat(tags.size()).isEqualTo(4);
            for (ResTag tag : tags) {
                System.out.println("   Tag ID: " + tag.getTagId() + ", Tag Name: " + tag.getTagName());
            }
        }
    }

    @Test
    void getRegisteredTag() {

    }

    @Test
    void modifyMemberTags() {
        Long savedMemberId = createMember(1);
        Member member = memberService.findMemberById(savedMemberId);
        tagService.saveMemberTags(member, List.of(1L, 2L, 3L));

        tagService.modifyMemberTags(member, List.of(4L, 5L, 6L));

        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(member);
        List<Long> registeredTagIds = registeredTag.stream().map(ResRegisteredTag::getTagId).collect(Collectors.toList());
        assertThat(registeredTagIds).isEqualTo(List.of(4L, 5L, 6L));
    }

    @Test
    void saveMemberTags() {
        Long savedMemberId = createMember(1);
        Member member = memberService.findMemberById(savedMemberId);
        tagService.saveMemberTags(member, List.of(1L, 2L, 3L));
        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(member);

        List<Long> registeredTagIds = registeredTag.stream().map(ResRegisteredTag::getTagId).collect(Collectors.toList());
        assertThat(registeredTagIds).isEqualTo(List.of(1L, 2L, 3L));
    }


    private Long createMember(int i) {
        GeneralSignUpReq member = GeneralSignUpReq.builder()
                .account("testAccount" + i)
                .password("testPassword")
                .name("testName")
                .build();

        GeneralSignUpRes generalSignUpRes = memberService.generalSignUp(member);
        return generalSignUpRes.getMemberId();
    }
}
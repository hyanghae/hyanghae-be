package flaskspring.demo.tag.service;

import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpRes;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.tag.dto.res.ResRegisteredTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
                // 이전에 선택된 태그를 초기화합니다.
                int randomPlaceId;
                do {
                    randomPlaceId = ThreadLocalRandom.current().nextInt(1, 741);
                } while (selectedPlace.contains(randomPlaceId)); // 이미 선택된 태그와 겹치지 않을 때까지 다시 뽑습니다.
                selectedPlace.add(randomPlaceId); // 선택된 태그를 기억합니다.
                System.out.println("insert into place_register(member_id, place_id, created_time) values(" + memberId + ", "+randomPlaceId+", now());");
                //System.out.println("insert into place_like(member_id, place_id) values(" + memberId + ", "+randomPlaceId+");");
                // System.out.println("INSERT INTO place_tag_log (place_id, tag_id) VALUES (" + placeId + ", " + randomTagId + ");");
            }
        }

    }

    @Test
    void getRegisteredTag() {

    }

    @Test
    void modifyMemberTags() {
        Long savedMemberId = createMember(1);
        tagService.saveMemberTags(savedMemberId, List.of(1L, 2L, 3L));

        tagService.modifyMemberTags(savedMemberId, List.of(4L, 5L, 6L));

        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(savedMemberId);
        List<Long> registeredTagIds = registeredTag.stream().map(ResRegisteredTag::getTagId).collect(Collectors.toList());
        assertThat(registeredTagIds).isEqualTo(List.of(4L, 5L, 6L));
    }

    @Test
    void saveMemberTags() {
        Long savedMemberId = createMember(1);
        tagService.saveMemberTags(savedMemberId, List.of(1L, 2L, 3L));
        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(savedMemberId);

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
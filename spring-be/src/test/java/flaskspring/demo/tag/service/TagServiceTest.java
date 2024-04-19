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
        Set<Integer> selectedTags = new HashSet<>();

        // SQL 문장 출력
        for (int placeId = 1; placeId <= 740; placeId++) {
            selectedTags.clear(); // 이전에 선택된 태그를 초기화합니다.
            for (int i = 0; i < 4; i++) {
                int randomTagId;
                do {
                    randomTagId = ThreadLocalRandom.current().nextInt(1, 25);
                } while (selectedTags.contains(randomTagId)); // 이미 선택된 태그와 겹치지 않을 때까지 다시 뽑습니다.
                selectedTags.add(randomTagId); // 선택된 태그를 기억합니다.
                System.out.println("INSERT INTO place_tag_log (place_id, tag_id) VALUES (" + placeId + ", " + randomTagId + ");");
            }
        }
    }

    @Test
    void getRegisteredTag() {

    }

    @Test
    void modifyMemberTags() {
        Long savedMemberId = createMember();
        tagService.saveMemberTags(savedMemberId, List.of(1L, 2L, 3L));

        tagService.modifyMemberTags(savedMemberId, List.of(4L, 5L, 6L));

        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(savedMemberId);
        List<Long> registeredTagIds = registeredTag.stream().map(ResRegisteredTag::getTagId).collect(Collectors.toList());
        assertThat(registeredTagIds).isEqualTo(List.of(4L, 5L, 6L));
    }

    @Test
    void saveMemberTags() {
        Long savedMemberId = createMember();
        tagService.saveMemberTags(savedMemberId, List.of(1L, 2L, 3L));
        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(savedMemberId);

        List<Long> registeredTagIds = registeredTag.stream().map(ResRegisteredTag::getTagId).collect(Collectors.toList());
        assertThat(registeredTagIds).isEqualTo(List.of(1L, 2L, 3L));
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
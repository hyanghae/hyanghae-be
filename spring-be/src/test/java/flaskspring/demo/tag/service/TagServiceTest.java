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

import java.util.List;
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
    void getRegisteredTag() {
        
    }

    @Test
    void modifyMemberTags() {
        Long savedMemberId = createMember();
        tagService.saveMemberTags(savedMemberId, List.of(1, 2, 3));

        tagService.modifyMemberTags(savedMemberId, List.of(4,5,6));

        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(savedMemberId);
        List<Long> registeredTagIds = registeredTag.stream().map(ResRegisteredTag::getTagId).collect(Collectors.toList());
        assertThat(registeredTagIds).isEqualTo(List.of(4L, 5L, 6L));
    }

    @Test
    void saveMemberTags() {
        Long savedMemberId = createMember();
        tagService.saveMemberTags(savedMemberId, List.of(1, 2, 3));
        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(savedMemberId);

        List<Long> registeredTagIds = registeredTag.stream().map(ResRegisteredTag::getTagId).collect(Collectors.toList());
        assertThat(registeredTagIds).isEqualTo(List.of(1L, 2L, 3L));
    }


    private Long createMember(){
        GeneralSignUpReq member = GeneralSignUpReq.builder()
                .account("testAccount")
                .password("testPassword")
                .name("testName")
                .build();

        GeneralSignUpRes generalSignUpRes = memberService.generalSignUp(member);
        return generalSignUpRes.getMemberId();
    }
}
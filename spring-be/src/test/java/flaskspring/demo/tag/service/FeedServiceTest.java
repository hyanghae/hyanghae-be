package flaskspring.demo.tag.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpRes;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.travel.domain.Place;
import flaskspring.demo.travel.dto.res.ResPlace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedServiceTest {

    @Autowired
    TagService tagService;

    @Autowired
    MemberService memberService;

    @Autowired
    FeedService feedService;

    @Test
    void getRecommendFeedTest() {
        Long savedMemberId = createMember();
        createMemberTags(savedMemberId, List.of(1L, 3L, 5L, 7L));

        List<ResPlace> recommendFeed = feedService.getRecommendFeed(savedMemberId, "alpha");
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
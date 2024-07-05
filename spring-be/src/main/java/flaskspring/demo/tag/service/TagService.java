package flaskspring.demo.tag.service;

import flaskspring.demo.config.redis.cache.EvictRedisCache;
import flaskspring.demo.config.redis.cache.RedisCacheable;
import flaskspring.demo.config.redis.cache.RedisCachedKeyParam;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.tag.domain.Category;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.tag.dto.res.ResCategoryTag;
import flaskspring.demo.tag.dto.res.ResRegisteredTag;
import flaskspring.demo.place.repository.MemberTagLogRepository;
import flaskspring.demo.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    @RedisCacheable(cacheName = "allTag", expireTime = 1440) //캐시 활용
    public List<ResCategoryTag> getAllTag() {

        List<Tag> allTagsWithCategory = tagRepository.findAllWithCategory();

        // 카테고리에 따라 태그를 그룹화
        Map<Category, List<Tag>> tagsGroupedByCategory = allTagsWithCategory.stream()
                .collect(Collectors.groupingBy(Tag::getCategory));

        List<ResCategoryTag> resCategoryTags = tagsGroupedByCategory.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Category::getId))) // 카테고리 Id로 정렬
                .map(entry -> new ResCategoryTag(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        return resCategoryTags;
    }

    @RedisCacheable(cacheName = "registeredTags", expireTime = 30) //캐시 활용
    public List<ResRegisteredTag> getRegisteredTag(@RedisCachedKeyParam(key = "member", fields = "memberId") Member member) {

        return memberService.getRegisteredTag(member)
                .stream()
                .map(ResRegisteredTag::new)
                .collect(Collectors.toList());
    } // 구현체 ArrayList여야 직렬화/역직렬화시 클래스 정보를 저장하고 에러가 나지 않음

    @EvictRedisCache(cacheName = "registeredTags") //캐시 삭제
    public void modifyMemberTags(@RedisCachedKeyParam(key = "member", fields = "memberId") Member member, List<Long> modifyTagIds) {
        // 기존에 등록된 태그 삭제
        memberTagLogRepository.deleteByMember(member);

        List<Tag> newTags = tagRepository.findByIdIn(modifyTagIds);

        // 새로운 태그 등록
        for (Tag tag : newTags) {
            boolean isExist = memberTagLogRepository.existsByMemberAndTag(member, tag);
            if (!isExist) {
                MemberTagLog memberTagLog = MemberTagLog.createMemberTagLog(member, tag);
                memberTagLogRepository.save(memberTagLog);
            }
        }
    }


    @Transactional
    @EvictRedisCache(cacheName = "registeredTags")
    public void saveMemberTags(@RedisCachedKeyParam(key = "member", fields = "memberId")Member member,
                               List<Long> tagIds) {

        member.onBoard(); // 온보딩 생애 최초
        // 변경된 태그 처리하기
        if (tagIds.isEmpty()) {
            // 추천 불가능
            member.canNotRecommend();

            // 기존에 등록된 태그 모두 삭제
            List<Tag> registeredTags = memberService.getRegisteredTag(member);
            Set<Long> removedTagIds = registeredTags.stream().map(Tag::getId).collect(Collectors.toSet());
            memberTagLogRepository.deleteByMemberAndTagIdIn(member, removedTagIds);

            // 태그가 삭제되었을 때만 refreshNeeded 설정
            if (!removedTagIds.isEmpty()) {
                member.setRefreshNeeded();
            }
        } else {
            // 추천 가능
            member.canRecommend();

            // 기존에 등록된 태그 가져오기
            List<Tag> registeredTags = memberService.getRegisteredTag(member);

            // 바뀐 태그만 처리하기 위한 작업
            Set<Long> newTagIds = new HashSet<>(tagIds);
            Set<Long> existingTagIds = registeredTags.stream().map(Tag::getId).collect(Collectors.toSet());

            // 새로 추가된 태그 처리
            Set<Long> addedTagIds = new HashSet<>(newTagIds);
            addedTagIds.removeAll(existingTagIds);
            for (Long addedTagId : addedTagIds) {
                Tag addedTag = tagRepository.findById(addedTagId)
                        .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
                MemberTagLog memberTagLog = MemberTagLog.createMemberTagLog(member, addedTag);
                memberTagLogRepository.save(memberTagLog);
            }

            // 기존에 등록된 태그 중에서 삭제된 태그 처리
            Set<Long> removedTagIds = new HashSet<>(existingTagIds);
            removedTagIds.removeAll(newTagIds);
            memberTagLogRepository.deleteByMemberAndTagIdIn(member, removedTagIds);

            // 변경 사항이 있을 경우에만 refreshNeeded 설정
            if (!addedTagIds.isEmpty() || !removedTagIds.isEmpty()) {
                member.setRefreshNeeded();
            }
        }
    }


}

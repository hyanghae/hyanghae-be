package flaskspring.demo.tag.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final MemberRepository memberRepository;

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

    public List<ResRegisteredTag> getRegisteredTag(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        List<MemberTagLog> memberTagLogs = memberTagLogRepository.findByMember(member);

        return memberTagLogs.stream()
                .map(MemberTagLog::getTag)
                .map(ResRegisteredTag::new)
                .toList();
    }

    public void modifyMemberTags(Long memberId, List<Long> modifyTagIds) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

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


    public void saveMemberTags(Long memberId, List<Long> tagIds) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        List<Tag> tags = tagRepository.findByIdIn(tagIds);

        for (Tag tag : tags) {
            boolean isExist = memberTagLogRepository.existsByMemberAndTag(member, tag);
            if (!isExist) {
                MemberTagLog memberTagLog = MemberTagLog.createMemberTagLog(member, tag);
                memberTagLogRepository.save(memberTagLog);
            }
        }
    }
}

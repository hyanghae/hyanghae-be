package flaskspring.demo.tag.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.tag.repository.MemberTagLogRepository;
import flaskspring.demo.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final MemberRepository memberRepository;


    public void saveMemberTags(Long memberId, List<Integer> tagIds) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        List<Tag> tags = tagRepository.findByIdIn(tagIds);

        for (Tag tag : tags) {

            MemberTagLog memberTagLog = MemberTagLog.createMemberTagLog(member, tag);
            memberTagLogRepository.save(memberTagLog);
        }
    }
}

package flaskspring.demo.member.service;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.dto.MemberDto;
import flaskspring.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    public Member createMember(MemberDto memberDto) {
        Member newMember = new Member();
        newMember.setName(memberDto.getName());
        newMember.setEmail(memberDto.getEmail());
        return memberRepository.save(newMember);
    }

    public Member updateMember(Long id, MemberDto memberDto) {
        Member existingMember = getMemberById(id);
        existingMember.setName(memberDto.getName());
        existingMember.setEmail(memberDto.getEmail());
        return memberRepository.save(existingMember);
    }

    public void deleteMember(Long id) {
        Member memberToDelete = getMemberById(id);
        memberRepository.delete(memberToDelete);
    }
}

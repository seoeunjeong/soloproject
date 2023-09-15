package soloproject.seomoim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soloproject.seomoim.domain.Member;
import soloproject.seomoim.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    

    public Long save(Member member){
        confirmIdDuplication(member);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    private void confirmIdDuplication(Member member) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()){
            throw new IllegalStateException("이미 존재하는 아이디 입니다");
        }
    }

    public void update(Long memberId){
        Member member = findMember(memberId);
        //변경감지 사용
    }
    
    public void delete(Long memberId){
        Member member = findMember(memberId);
        memberRepository.delete(member);
    }

    public Member findMember(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        return findMember.orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다"));
    }
}

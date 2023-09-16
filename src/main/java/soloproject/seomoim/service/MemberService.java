package soloproject.seomoim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import soloproject.seomoim.domain.Member;
import soloproject.seomoim.dto.MemberDto;
import soloproject.seomoim.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    

    public Long signup(Member member){
        //아이디 중복 확인
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

    @Transactional
    public void update(Long memberId, Member member){
        Member findmember = findMember(memberId);

        Optional.ofNullable(member.getAge())
                .ifPresent(age-> findmember.setAge(age));
        Optional.ofNullable(member.getName())
                .ifPresent(name-> findmember.setName(name));
        Optional.ofNullable(member.getGender())
                .ifPresent(gender-> findmember.setGender(gender));
        Optional.ofNullable(member.getRegion())
                .ifPresent(region-> findmember.setRegion(region));
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

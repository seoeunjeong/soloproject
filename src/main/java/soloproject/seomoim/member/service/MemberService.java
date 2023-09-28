package soloproject.seomoim.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.moim.entitiy.MoimMember;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long signup(Member member){
        //아이디 중복 확인
        confirmIdDuplication(member);
        //패스워드 일치 확인

        //패스워드 암호화

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Transactional
    public void update(Long memberId, Member member){
        //변경감지 사용
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
        return findMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void confirmIdDuplication(Member member) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()){
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_ID);
        }
    }

    //회원이 참여한 모임 조회
    public List<MoimMember> findParticipationMoim(Long memberId){
        List<MoimMember> participationMoims = memberRepository.findByParticipationMoims(memberId);

        return participationMoims;
    }

    //회원 정보와 함께 참여한 모임 조회

    public Member findMemberAndfindParticipationMoim(Long memberId){
        Member findMember = memberRepository.findByIdAndParticipationMoims(memberId);
        return findMember;
    }
}

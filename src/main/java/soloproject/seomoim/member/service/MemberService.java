package soloproject.seomoim.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import soloproject.seomoim.KakaoApi.dto.DocumentDto;
import soloproject.seomoim.KakaoApi.dto.KakaoApiResponseDto;
import soloproject.seomoim.KakaoApi.service.KakaoAddressSearchService;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;
import soloproject.seomoim.moim.repository.MoimMemberRepository;
import soloproject.seomoim.moim.repository.MoimRepository;
import soloproject.seomoim.security.CustomAuthorityUtils;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final MoimMemberRepository moimMemberRepository;
    private final MoimRepository moimRepository;

    @Transactional
    public Long signup(Member member) {
        //아이디 중복 확인
        confirmIdDuplication(member);
        //패스워드 일치 확인
        if (!member.getPassword().equals(member.getConfirmPassword())) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_MISMATCH);
        }
        //패스워드 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());

        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    @Transactional
    public void update(Long memberId, Member member) {
        //변경감지 사용
        Member findmember = findMember(memberId);
        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> findmember.getPassword());
        Optional.ofNullable(member.getName())
                .ifPresent(name -> findmember.setName(name));
        Optional.ofNullable(member.getAge())
                .ifPresent(age -> findmember.setAge(age));
        Optional.ofNullable(member.getGender())
                .ifPresent(gender -> findmember.setGender(gender));
        Optional.ofNullable(member.getAddress())
                .ifPresent(address -> findmember.setAddress(address));
        if(member.getAddress()!=null){
            KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(member.getAddress());
            DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
            findmember.setLatitude(documentDto.getLatitude());
            findmember.setLongitude(documentDto.getLongitude());
        }
    }

    @Transactional
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

    /*회원이 자신이 참여한 모임 조회*/
    public List<Moim> findParticipationMoims(Long memberId){
        List<MoimMember> participationMoims = moimMemberRepository.findParticipatingMoims(memberId);
        List<Moim> moims = participationMoims.stream()
                .map(moimMember -> moimMember.getMoim().getId())
                .map(moimId -> moimRepository.findById(moimId).orElse(null))
                .collect(Collectors.toList());
        return moims;
    }

    //회원 정보와 함께 참여한 모임 조회

//    public Member findMemberAndfindParticipationMoim(Long memberId){
//        Member findMember = memberRepository.findByIdAndParticipationMoims(memberId);
//        return findMember;
//    }


}

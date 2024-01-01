package soloproject.seomoim.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import soloproject.seomoim.KakaoApi.dto.DocumentDto;
import soloproject.seomoim.KakaoApi.dto.KakaoApiResponseDto;
import soloproject.seomoim.KakaoApi.service.KakaoAddressSearchService;
import soloproject.seomoim.advice.exception.BusinessLogicException;
import soloproject.seomoim.advice.exception.ClientRequestException;
import soloproject.seomoim.advice.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.security.FormLogin.CustomAuthorityUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final KakaoAddressSearchService kakaoAddressSearchService;

    @Transactional
    public Long signup(Member member) {
        checkIdDuplication(member);

        if (!member.getPassword().equals(member.getConfirmPassword())) {
            throw new ClientRequestException(ExceptionCode.PASSWORD_MISMATCH);
        }

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
                .ifPresent(findmember::setPassword);
        Optional.ofNullable(member.getName())
                .ifPresent(findmember::setName);
        Optional.ofNullable(member.getAge())
                .ifPresent(findmember::setAge);
        Optional.ofNullable(member.getGender())
                .ifPresent(findmember::setGender);
        Optional.ofNullable(member.getRoles())
                .filter(roles -> !roles.isEmpty())
                .ifPresent(findmember::setRoles);

        /*빈 문자열 들어온다*/
        if (member.getAddress() != null && !member.getAddress().equals("")) {
            KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(member.getAddress());
            DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
            log.info("documentDto={}",documentDto.toString());
            findmember.setAddress(member.getAddress());
            findmember.setLatitude(documentDto.getLatitude());
            findmember.setLongitude(documentDto.getLongitude());
            findmember.setAddressDong(documentDto.getRegion3DepthName());
        }
    }

    @Transactional
    public void delete(String email,Long memberId){
        Member loginMember = findByEmail(email);
        Member deleteMember = findMember(memberId);
        if(loginMember!=deleteMember){
            throw new BusinessLogicException(ExceptionCode.INVALID_REQUEST);
        }
        memberRepository.delete(deleteMember);
    }


    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    public Member findByEmail(String email){
         return memberRepository.findByEmail(email)
                 .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void checkIdDuplication(Member member) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()){
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_ID);
        }
    }

/*
    //회원 정보와 함께 참여한 모임 조회
//    public Member findMemberAndfindParticipationMoim(Long memberId){
//        Member findMember = memberRepository.findByIdAndParticipationMoims(memberId);
//        return findMember;

//    }
//    회원이 자신이 참여한 모임 조회
//    public List<Moim> findParticipationMoims(Long memberId){
//        List<MoimMember> participationMoims = moimMemberRepository.findJoinMoims(memberId);
//        List<Moim> moims = participationMoims.stream()
//                .map(moimMember -> moimMember.getMoim().getId())
//                .map(moimId -> moimRepository.findById(moimId).orElse(null))
//                .collect(Collectors.toList());
//        return moims;
//    }
*/

}

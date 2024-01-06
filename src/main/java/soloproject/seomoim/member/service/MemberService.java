package soloproject.seomoim.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import soloproject.seomoim.KakaoApi.dto.DocumentDto;
import soloproject.seomoim.KakaoApi.dto.KakaoApiResponseDto;
import soloproject.seomoim.KakaoApi.service.KakaoAddressSearchService;
import soloproject.seomoim.advice.exception.BusinessLogicException;
import soloproject.seomoim.advice.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.profileImage.ProfileImageUploadService;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.security.FormLogin.CustomAuthorityUtils;

import java.io.IOException;
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
    private final ProfileImageUploadService profileImageUploadService;

    @Transactional
    public Long signup(Member member) {

        checkIdDuplication(member.getEmail());

        if (!member.getPassword().equals(member.getConfirmPassword())) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_MISMATCH);
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
        Member findMember = findMemberById(memberId);

        Optional.ofNullable(member.getName())
                .ifPresent(findMember::setName);
        Optional.ofNullable(member.getAge())
                .ifPresent(findMember::setAge);
        Optional.ofNullable(member.getGender())
                .ifPresent(findMember::setGender);
        Optional.ofNullable(member.getRoles())
                .filter(roles -> !roles.isEmpty())
                .ifPresent(findMember::setRoles);

        if (StringUtils.hasText(member.getAddress())) {
            KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(member.getAddress())
                    .orElseThrow(() -> new IllegalStateException("카카오 주소 요청이 실패했습니다.다른장소를 입력해주세요."));
            DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
            log.info("documentDto={}", documentDto);
            findMember.setAddress(member.getAddress());
            findMember.setLatitude(documentDto.getLatitude());
            findMember.setLongitude(documentDto.getLongitude());
            findMember.setEupMyeonDong(documentDto.getRegion3DepthName());
        }

    }

    @Transactional
    public void updateProfileImage(Long memberId,MultipartFile profileImage){
        Member findMember = findMemberById(memberId);
        log.info("profileImamge={}",profileImage);
        if (profileImage!=null) {
            try {
                profileImageUploadService.uploadFileToGCS(profileImage,findMember);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            profileImageUploadService.deleteFile(findMember);
        }
    }

    @Transactional
    public void delete(String loginMemberEmail, Long deleteMemberId) {
        Member loginMember = findMemberByEmail(loginMemberEmail);
        Member deleteMember = findMemberById(deleteMemberId);

        if (loginMember != deleteMember) {
            throw new BusinessLogicException(ExceptionCode.INVALID_REQUEST);
        }
        //todo ! 회원탈퇴할때 어떤 정보가 있더 무조건 탈퇴,관련데이터 전부 지우기
        memberRepository.delete(deleteMember);
    }


    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    public Member findMemberByEmail(String email){
         return memberRepository.findByEmail(email)
                 .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void checkIdDuplication(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_ID);
        }
    }

}

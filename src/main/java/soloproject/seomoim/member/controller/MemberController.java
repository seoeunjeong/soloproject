package soloproject.seomoim.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.advice.exception.BusinessLogicException;
import soloproject.seomoim.advice.exception.ClientRequestException;
import soloproject.seomoim.member.loginCheck.AuthenticationdUser;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.profileImage.ProfileImageUploadService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProfileImageUploadService profileImageUploadService;
    private final MemberMapper mapper;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @GetMapping("/login-form")
    public String loginForm(HttpServletRequest request, Model model) {
        String referer = request.getHeader("Referer");
        if (referer != null && referer.equals("http://localhost:8080/members")) {
            model.addAttribute("success", true);
        }
        return "members/loginForm";
    }

    @GetMapping("/signup-form")
    public String signUpForm(Model model) {
        model.addAttribute("signup", new MemberDto.Signup());
        return "members/signupForm";
    }

    @PostMapping("/members")
    public String signUp(@Valid @ModelAttribute MemberDto.Signup signup,
                         BindingResult bindingResult,
                         Model model) {

        log.info("sinupDto={}",signup.getEmail());
        if (bindingResult.hasErrors()) {
            return "members/signupForm";
        }

        try {
            memberService.signup(mapper.memberSignUpDtoToMember(signup));

        } catch (BusinessLogicException | ClientRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "members/signupForm";
        }

        model.addAttribute("email", signup.getEmail());

        return "members/emailAuthForm";
    }

    @GetMapping("/members/edit_form")
    public String myPageEditFrom(@AuthenticationdUser String email,
                                 @RequestParam(required = false) Boolean status,
                                 Model model) {
        Member member = memberService.findByEmail(email);
        model.addAttribute("member", member);
        model.addAttribute("status", status);
        return "members/editForm";
    }


    @PostMapping("/members/{member-id}")
    public String updateProfile(@PathVariable("member-id") Long memberId,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute MemberDto.Update request) {
        Member findMember = memberService.findMember(memberId);

        Member member = mapper.memberUpdateDtoToMember(request);

        log.info("request.getProfileImage={}", request.getProfileImage());
        if (!request.getProfileImage().isEmpty()) {
            try {
                profileImageUploadService.uploadFileToGCS(request.getProfileImage(), findMember);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            profileImageUploadService.deleteFile(findMember);
        }

        memberService.update(memberId, member);
        redirectAttributes.addAttribute("status", true);

        return "redirect:/members/edit_form";
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/members/{member-id}")
    public void DeleteMember(@PathVariable("member-id") Long memberId,
                             @AuthenticationdUser String email) {
        log.info("email={}",email);
        log.info("memberId={}",memberId);
        Member findMember = memberService.findMember(memberId);
        log.info("findMember={}",findMember);
        memberService.delete(email,findMember.getId());
    }


    @GetMapping("/members/logout")
    public String googleLogout(Authentication authentication,HttpServletRequest request) {

        OAuth2AuthorizedClient google = oAuth2AuthorizedClientService.loadAuthorizedClient("google", authentication.getName());

        if (google != null && google.getAccessToken() != null) {
            String tokenValue = google.getAccessToken().getTokenValue();

            revokeGoogleToken(tokenValue);
        }

        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }

    private static void revokeGoogleToken(String accessToken) {
        // Google Revoke Token 엔드포인트 URL
        String revokeTokenUrl = "https://accounts.google.com/o/oauth2/revoke";

        // Google OAuth 토큰을 무효화하는데 필요한 파라미터
        String tokenParameter = "token=" + accessToken;

        // Request Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Request Entity 생성
        RequestEntity<String> requestEntity = new RequestEntity<>(tokenParameter, headers, HttpMethod.POST, URI.create(revokeTokenUrl));

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Google에 토큰 무효화 요청 보내기
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            // 성공적으로 무효화되면 Google 서버는 200 OK를 응답할 것입니다.
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                System.out.println("Google OAuth Token successfully revoked.");
            } else {
                System.out.println("Failed to revoke Google OAuth Token. Response: " + responseEntity.getBody());
            }
        } catch (HttpClientErrorException e) {
            // Google 서버에서 오류 응답이 온 경우
            System.out.println("Failed to revoke Google OAuth Token. Error response: " + e.getResponseBodyAsString());
        }

    }
}

//    /* 회원이 만든 모임 list 조회 */
//    //members/{member-id}/moims/
//    @GetMapping("members/{member-id}/moims")
//    public String findCreatedMoimList(@PathVariable("member-id")Long memberId,
//                                      Model model){
//        Member member = memberService.findMember(memberId);
//        List<Moim> createdMoimList = moimService.findMyCreatedMoim(memberId);
//        model.addAttribute("MoimList", createdMoimList);
//        return null;
//    }
//
//
//    /* 회원이 참여한 모임 List 조회 */
//    //members/moims/{member-id}
//    @GetMapping("members/moims/{member-id}")
//    public String findJoinedMoimList(@PathVariable("member-id")Long memberId,Model model){
//        List<Moim> participationMoims = memberService.findParticipationMoims(memberId);
//        model.addAttribute("moimlist",participationMoims);;
//
//        return "moims/participationMoims";
//    }
//
////
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.advice.exception.BusinessLogicException;
import soloproject.seomoim.member.loginCheck.LoginMember;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.service.LatestViewMoimService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final LatestViewMoimService latestViewService;
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
    public String signUp(@Valid @ModelAttribute MemberDto.Signup signup, BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            return "members/signupForm";
        }
        try {
            memberService.signup(mapper.memberSignUpDtoToMember(signup));
        } catch (BusinessLogicException e) {
            model.addAttribute("error", e.getMessage());
            return "members/signupForm";
        }

        model.addAttribute("email", signup.getEmail());
        return "members/emailAuthForm";
    }

    @GetMapping("/members/edit_form")
    public String myPageEditFrom(@LoginMember String email,
                                 @RequestParam(required = false) Boolean status,
                                 Model model) {
        Member member = memberService.findMemberByEmail(email);
        MemberDto.Update UpdateDto = mapper.memberToMemberUpdateDto(member);
        model.addAttribute("update", UpdateDto);
        model.addAttribute("profileUrl",member.getProfileImage());
        model.addAttribute("loginMemberId",member.getId());
        model.addAttribute("status", status);
        return "members/editForm";
    }


    @PostMapping("/members/{member-id}")
    public String updateProfile(@PathVariable("member-id") Long memberId,
                                @Valid @ModelAttribute MemberDto.Update request, BindingResult bindingResult,
                                Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMemberId",memberId);
            return "members/editForm";
        }

        Member member = mapper.memberUpdateDtoToMember(request);
        memberService.update(memberId, member);

        redirectAttributes.addAttribute("status", true);

        return "redirect:/members/edit_form";
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/members/profileImage/{member-id}")
    public void updateProfileImage(@PathVariable("member-id") Long memberId,
                                     @RequestParam(value = "profileImage") MultipartFile profileImage) {
        if (profileImage != null && !profileImage.isEmpty()) {
            memberService.updateProfileImage(memberId, profileImage);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/members/profileImage/{member-id}")
    public void deleteProfileImage(@PathVariable("member-id") Long memberId,
                                   @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        memberService.updateProfileImage(memberId, profileImage);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/members/{member-id}")
    public void deleteMember(@PathVariable("member-id") Long memberId,
                             @LoginMember String loginMemberEmail,
                             HttpServletRequest request) {
        memberService.delete(loginMemberEmail,memberId);

        HttpSession session = request.getSession();
        session.invalidate();

        latestViewService.deleteLatestPostsForMeember(memberId,5);


    }


    @GetMapping("/members/logout")
    public String logout(Authentication authentication,HttpServletRequest request) {

        OAuth2AuthorizedClient google = oAuth2AuthorizedClientService.loadAuthorizedClient("google", authentication.getName());

        if (google != null && google.getAccessToken() != null) {
            String tokenValue = google.getAccessToken().getTokenValue();
            revokeGoogleToken(tokenValue);
        }

        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }

    //구글 oauth 토큰 무효화
    private static void revokeGoogleToken(String accessToken) {
        String revokeTokenUrl = "https://accounts.google.com/o/oauth2/revoke";
        String body = "token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(revokeTokenUrl));

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Google OAuth Token 무효화 성공");
            } else {
                log.info("Google OAuth Token 무효화 실패. Response: " + responseEntity.getBody());
            }
        } catch (HttpClientErrorException e) {
                log.info("Google OAuth Token 무효화 요청 중 예외발생 : " + e.getResponseBodyAsString());
        }
    }
}

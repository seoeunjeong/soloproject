package soloproject.seomoim.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ClientRequestException;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.profileImage.ProfileImageUploadService;
import soloproject.seomoim.security.FormLogin.CustomUserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProfileImageUploadService profileImageUploadService;
    private final MemberMapper mapper;

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
    public String myPageEditFrom(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam(required = false) Boolean status,
                                 Model model){
        Member member = memberService.findByEmail(userDetails.getEmail());
        model.addAttribute("member", member);
        model.addAttribute("status",status);
        return "members/editForm";
    }


    @PostMapping("/members/{member-id}")
    public String updateProfile(@PathVariable("member-id") Long memberId,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute MemberDto.Update request){
        Member findMember = memberService.findMember(memberId);

        Member member = mapper.memberUpdateDtoToMember(request);

        log.info("request.getProfileImage={}",request.getProfileImage());
        if (!request.getProfileImage().isEmpty()) {
            try {
                profileImageUploadService.uploadFileToGCS(request.getProfileImage(), findMember);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            //비어있는 요청을 했을때 버킷의 객체는 삭제하는데
            profileImageUploadService.deleteFile(findMember);
        }

        memberService.update(memberId,member);
        redirectAttributes.addAttribute("status", true);

        return "redirect:/members/edit_form";
    }



}


//    @GetMapping("/auth-form")
//    public String authLoginForm() {
//        return "members/loginForm";
//    }
//
//    /*로그아웃확인*/
//    @GetMapping("members/logout")
//    public String logoutMember(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(false);
//        if(session!=null)
//            session.invalidate();
//        return "redirect:/";
//    }

//
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
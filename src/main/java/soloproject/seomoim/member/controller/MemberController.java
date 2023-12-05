package soloproject.seomoim.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.profileImage.ImageUploadService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;
    private final MoimService moimService;
    private final ImageUploadService imageUploadService;
    private static final String IMAGE_DEFAULT_URL= "https://storage.googleapis.com/seomoim/";
    @GetMapping("/login-form")
    public String loginForm(HttpServletRequest request,Model model){
        String referer = request.getHeader("Referer");

        if(referer!=null&& referer.equals("http://localhost:8080/members")){
            model.addAttribute("success","signupSuccess");
        }
        return "members/loginForm";
    }
    @GetMapping("/auth-form")
    public String authLoginForm() {
        return "members/loginForm";
    }

    @GetMapping("/signup-form")
    public String signUpForm(Model model) {
        model.addAttribute("signup",new MemberDto.Signup());
        return "members/signupForm";
    }

    @PostMapping("/members")
    public String signUp(@Valid @ModelAttribute MemberDto.Signup signup,
                         BindingResult bindingResult,
                         Model model) {

        /*검증실패시 리다이렉트말고 다시 가입폼으로 이동 /컨트롤러 호출 안하고 바로 페이지이동해야 bindingResult담긴다*/
        if (bindingResult.hasErrors()) {
            return "members/signupForm";
        }
        Long signupId = memberService.signup(mapper.memberSignUpDtoToMember(signup));

        model.addAttribute("memberId", signupId);
        model.addAttribute("email", signup.getEmail());

        return "members/emailAuthForm";
    }

    @GetMapping("/members/edit_form")
    public String myPageEditFrom(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model,
                                 @RequestParam(value = "status",required = false) Boolean status) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        model.addAttribute("member", member);
        model.addAttribute("status",status);
        return "members/editForm";
    }

    // 프로필 수정시 이미지 전송
    @PostMapping("/members/{member-id}")
    public String updateProfile(@PathVariable("member-id") Long memberId,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute MemberDto.Update request) throws IOException {
        Member member = mapper.memberUpdateDtoToMember(request);
        if(request.getFile()!=null) {
            String imageUuid = imageUploadService.uploadFileToGCS(request.getFile());
            member.setProfileImgUri(IMAGE_DEFAULT_URL + imageUuid);
            memberService.update(memberId, member);
        }else{
            memberService.update(memberId,member);
        }
        redirectAttributes.addAttribute("status", true);
        //해당 memberId 상세페이지로 이동
        return "redirect:/members/edit_form";
    }

    /* 회원이 만든 모임 list 조회 */
    //members/{member-id}/moims/
    @GetMapping("members/{member-id}/moims")
    public String findCreatedMoimList(@PathVariable("member-id")Long memberId,
                                      Model model){
        Member member = memberService.findMember(memberId);
        List<Moim> createdMoimList = moimService.findCreatedMoim(memberId);
        model.addAttribute("MoimList", createdMoimList);
        return null;
    }
    /* 회원이 참여한 모암 List 조회 */
    //members/moims/{member-id}
    @GetMapping("members/moims/{member-id}")
    public String findJoinedMoimList(@PathVariable("member-id")Long memberId,Model model){
        List<Moim> participationMoims = memberService.findParticipationMoims(memberId);
        model.addAttribute("moimlist",participationMoims);;

        return "moims/participationMoims";
    }

    /*로그아웃확인*/
    @GetMapping("members/logout")
    public String logoutMember(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if(session!=null)
            session.invalidate();
        return "redirect:/";
    }



}

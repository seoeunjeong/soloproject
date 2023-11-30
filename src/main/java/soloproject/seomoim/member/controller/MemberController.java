package soloproject.seomoim.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;
    private final MoimService moimService;

    @GetMapping("/loginFrom")
    public String loginForm() {
        return "members/login";
    }

    @GetMapping("auth/loginFrom")
    public String authLoginForm() {
        return "members/loginForm";
    }

    @GetMapping
    public String signUpForm() {
        return "members/signupFrom";
    }

    @PostMapping
    public String signUp(@Valid @ModelAttribute MemberDto.Signup request) {
        Long signupId = memberService.signup(mapper.memberSignUpDtoToMember(request));
        return "redirect:/";
    }

    @GetMapping("/{member-id}")
    public String myPageFrom(@PathVariable("member-id") Long memberId,
                            Model model){
        Member member = memberService.findMember(memberId);
        model.addAttribute("member",member);
        List<Moim> createdMoim = moimService.findCreatedMoim(memberId);
        model.addAttribute("moimList",createdMoim);
        return "members/myPage";

    }

    @GetMapping("/edit/{member-id}")
    public String myPageEditFrom(@PathVariable("member-id") Long memberId,
                                  Model model) {
        Member member = memberService.findMember(memberId);
        model.addAttribute("member", member);
        return "members/editFrom";
    }

    @PostMapping("/edit/{member-id}")
    public String updateProfile(@PathVariable("member-id") Long memberId,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute MemberDto.Update request) {
        Member findMember = memberService.findMember(memberId);
        Member member = mapper.memberUpdateDtoToMember(request);
        memberService.update(memberId, member);
        redirectAttributes.addAttribute("memberId", memberId);
        redirectAttributes.addAttribute("status", true);
        //해당 memberId 상세페이지로 이동
        return "redirect:/members/{memberId}";
    }

    /* 회원이 만든 모임 list 조회 */
    //members/{member-id}/moims/
    @GetMapping("/{member-id}/moims")
    public String findCreatedMoimList(@PathVariable("member-id")Long memberId,
                                      Model model){
        Member member = memberService.findMember(memberId);
        List<Moim> createdMoimList = moimService.findCreatedMoim(memberId);
        model.addAttribute("MoimList", createdMoimList);
        return null;
    }
    /* 회원이 참여한 모암 List 조회 */
    //members/moims/{member-id}
    @GetMapping("/moims/{member-id}")
    public String findJoinedMoimList(@PathVariable("member-id")Long memberId,Model model){
        List<Moim> participationMoims = memberService.findParticipationMoims(memberId);
        model.addAttribute("moimlist",participationMoims);;

        return "moims/participationMoims";
    }

    /*로그아웃확인*/
    @GetMapping("/logout")
    public String logoutMember(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if(session!=null)
            session.invalidate();
        return "redirect:/";
    }

}

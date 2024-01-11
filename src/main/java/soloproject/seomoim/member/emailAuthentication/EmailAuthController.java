package soloproject.seomoim.member.emailAuthentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.loginCheck.LoginMember;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.utils.RedisUtil;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class EmailAuthController {

    private final MailSendService mailsendService;
    private final MemberService memberService;
    private final RedisUtil redisUtil;

    @GetMapping("/email/auth-form")
    public String emailAuthFrom(@LoginMember String loginMemberEmail, Model model) {
        Member loginMember = memberService.findMemberByEmail(loginMemberEmail);
        List<String> roles = loginMember.getRoles();
        if (roles.get(0).equals("AUTH_USER")) {
            throw new IllegalStateException("이미 인증된 회원입니다");
        }
        String email = loginMember.getEmail();
        model.addAttribute("email", email);
        return "members/emailAuthForm";
    }

    @GetMapping("/email")
    public String MailSend(String email, Model model) {
        try {
            mailsendService.sendMail(email);
        } catch (Exception e) {
            model.addAttribute("sendError", e.getMessage());
        }
        model.addAttribute("send", "success");
        model.addAttribute("email", email);
        return "members/emailAuthForm";
    }

    //인증시 재로그인 유도
    @PostMapping("/email/auth")
    public String authEmail(@RequestParam String verificationCode,
                            @RequestParam String email,
                            Model model) {
        String sendCode = (String) redisUtil.get(email);
        log.info("sendNumber=" + sendCode);
        if(sendCode==null){
            model.addAttribute("email",email);
            model.addAttribute("expire","인증시간이 만료되었습니다.");
            return "members/emailAuthForm";
        }

        if (verificationCode.equals(sendCode)) {
            Member findMember = memberService.findMemberByEmail(email);
            findMember.setRoles(List.of("AUTH_USER"));
            memberService.update(findMember.getId(), findMember);
            model.addAttribute("authSuccess","인증이 완료되었습니다 로그인 해주세요.");
            return "members/loginForm";

        } else {
            model.addAttribute("error", "인증실패");
            model.addAttribute("email", email);

            return "members/emailAuthForm";
        }
    }
}

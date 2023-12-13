package soloproject.seomoim.emailAuthentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.security.FormLogin.CustomUserDetails;
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
    public String emailAuthFrom(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<String> roles = userDetails.getRoles();
        if (roles.get(0).equals("AUTH_USER")) {

            throw new IllegalStateException("이미 인증된 회원입니다");

        }
        String email = userDetails.getEmail();
        model.addAttribute("email", email);
        return "members/emailAuthForm";
    }

    /*인증번호 발송 api*/
    @GetMapping("/email")
    public String MailSend(String email, Model model){
        mailsendService.sendMail(email);
        model.addAttribute("send","success");
        model.addAttribute("email",email);
        return "members/emailAuthForm";
    }


    //재로그인 유도
    @PostMapping("/email/auth")
    public String authEmail(@RequestParam String number,
                            @RequestParam String email,
                            Model model) {
        String sendNumber = (String) redisUtil.get(email);
        log.info("sendNumber=" + sendNumber);
        log.info("number=" + number);

        if (number.equals(sendNumber)) {
            Member findMember = memberService.findByEmail(email);
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

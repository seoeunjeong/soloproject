package soloproject.seomoim.emailAuthentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.member.entity.Member;
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

    @GetMapping("/email/authForm")
    public String emailAuthFrom(@RequestParam(required = false) String email, Model model) {
        model.addAttribute("email", email);
        return "members/emailAuthForm";
    }

    /*인증번호 발송 api*/
    @GetMapping("/email")
    public String MailSend(String email, RedirectAttributes redirectAttributes){
        mailsendService.sendMail(email);
        redirectAttributes.addAttribute("send","success");
        redirectAttributes.addAttribute("email",email);
        return "redirect:email_authFrom";
    }

    /* 인증번호와 입력인증번호를 비교해서 회원 역할 업데이트 */
    @PostMapping("/email/auth")
    public String authEmail(@RequestParam String number,
                            @RequestParam String email,RedirectAttributes redirectAttributes) {
        String sendNumber = (String) redisUtil.get(email);
        log.info("sendNumber=" + sendNumber);
        log.info("number=" + number);
        String number2 = String.valueOf(number);

        /*todo 인증완료되면 로그인해서 홈으로 보내줘야지*/
        if (number2.equals(sendNumber)) {
            Member findMember = memberService.findByEmail(email);
            findMember.setRoles(List.of("AUTH_USER"));
            memberService.update(findMember.getId(),findMember);
            return "redirect:/";
        }else{
            redirectAttributes.addAttribute("error", "인증실패");
            redirectAttributes.addAttribute("email",email);
            return "redirect:/email_authFrom";
        }
    }
}

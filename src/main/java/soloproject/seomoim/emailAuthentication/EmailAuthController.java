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

    private final RedisUtil redisUtil;

    private final MemberService memberService;

    @GetMapping("/members/authFrom")
    public String emailAuthFrom(@RequestParam(required = false) Long memberId,
                                @RequestParam(required = false) String email,
                                Model model) {
        model.addAttribute("memberId", memberId);
        model.addAttribute("email", email);
        return "/members/emailAuthFrom";
    }

    /*인증번호 발송 api*/
    @GetMapping("/email")
    public String MailSend(String email, RedirectAttributes redirectAttributes){
        mailsendService.sendMail(email);
        redirectAttributes.addAttribute("email",email);
        return "redirect:members/authFrom";
    }

    /* 인증시 발급인증번호와 입력인증번호를 비교해서 회원 역할 업데이트 */
    @PostMapping("/email/auth")
    @ResponseBody
    public String authEmail(@RequestParam String number,
                            @RequestParam String email) {
        String sendNumber = (String) redisUtil.get(email);
        log.info("sendNumber=" + sendNumber);
        log.info("number=" + number);
        String number2 = String.valueOf(number);

        if (number2.equals(sendNumber)) {
            Member findMember = memberService.findByEmail(email);
            findMember.setRoles(List.of("AUTH_USER"));
            memberService.update(findMember.getId(),findMember);
            return "인증이완료되었습니다습니다";
        }
        return "인증이 실패하였습니다.";
    }
}

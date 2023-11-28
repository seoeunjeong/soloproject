package soloproject.seomoim.member.emailCertification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import soloproject.seomoim.utils.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final MailService mailService;

    @GetMapping("/email")
    public String MailSend(String email, HttpServletRequest request){
        mailService.createMail(email);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.AUTHENTICATION_MEMBER,email);

        return "인증메일을 발송했습니다";
    }
}

package soloproject.seomoim.member.email;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final MailService mailService;

    @PostMapping("/email")
    public void MailSend(String email){
        mailService.createMail(email);
    }




}

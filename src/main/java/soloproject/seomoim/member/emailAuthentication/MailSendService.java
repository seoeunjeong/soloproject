package soloproject.seomoim.member.emailAuthentication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import soloproject.seomoim.advice.exception.BusinessLogicException;
import soloproject.seomoim.advice.exception.ExceptionCode;
import soloproject.seomoim.utils.RedisUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailSendService {

    @Value("${spring.mail.username}")
    private String senderEmail;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    public void sendMail(String recipientEmail) {
        int verificationCode = createVerificationCode();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, recipientEmail);
            message.setSubject("SEOMOIM 이메일인증번호");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + verificationCode + "</h1>";
            body += "<p>" + "이메일 인증화면에서 인증번호를 입력해주세요." + "</p>";
            body += "<p>" + "감사합니다." + "</p>";
            message.setText(body,"UTF-8", "html");
            javaMailSender.send(message);
        }catch (MessagingException e) {
            throw new BusinessLogicException(ExceptionCode.MESSAGE_FAIL);
        }

        String sendCode = String.valueOf(verificationCode);
        redisUtil.set(recipientEmail,sendCode,3);
    }

    /*todo! 랜덤번호 안전하게 생성*/
    private static int createVerificationCode() {
        return (int) (Math.random() * (90000)) + 100000;
    }
}

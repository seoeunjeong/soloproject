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

    public void sendMail(String email) {
        int number = createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("회원가입 인증번호 발송");
            message.setText("회원가입화면으로 돌아가 인증번호를 입력해주세요");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "회원가입 화면에서 인증번호를 입력해주세요." + "</h3>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");

        } catch (MessagingException e) {
            throw new BusinessLogicException(ExceptionCode.MESSAGE_FAIL);
        }
        javaMailSender.send(message);
        String sendNumber = String.valueOf(number);
        redisUtil.set(email,sendNumber,3);
//        email을 키로 저장 ㅎㅎㅎㅎㅎ
    }

    /*todo! 랜덤번호 안전하게 생성*/
    private int createNumber() {

        return (int) (Math.random() * (90000)) + 100000;
    }


}

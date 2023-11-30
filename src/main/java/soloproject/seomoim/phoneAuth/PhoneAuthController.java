package soloproject.seomoim.phoneAuth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PhoneAuthController {

    @GetMapping("/members/phone")
    public String phoneAuth(@RequestParam Long memberId,
                            Model model) {
        model.addAttribute("memberId", memberId);

        return "members/phone";
    }

    /*인증번호 발송 api*/

    /*인증확인 api-회원등급 업데이트 인증된회원만 참여가능 ㅎㅎㅎ/

     */

}
package soloproject.seomoim;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.utils.SessionConst;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MoimService moimService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        //세션에 회원데이터가 없으면 home
        if (loginMember == null) {
            List<Moim> all = moimService.findAll();
            model.addAttribute("moims", all);
            return "home/home";
        }
        //세션이 유지되면 로그인홈 으로 이동
        model.addAttribute("member", loginMember);
        List<Moim> all = moimService.findAll();
        model.addAttribute("moims", all);
        return "home/loginHome";
    }
}


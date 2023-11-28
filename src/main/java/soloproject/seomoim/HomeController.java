package soloproject.seomoim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.security.CustomUserDetails;
import soloproject.seomoim.security.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MoimService moimService;
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        log.info("member" + userDetails);
        List<Moim> moims = moimService.findAll();

        List<Moim> flashedMoims = (List<Moim>) model.getAttribute("moims");

        if (userDetails == null) {
            model.addAttribute("moims", moims);
            return "home/home";
        }

        if (flashedMoims != null) {
            moims = flashedMoims;
        }

        model.addAttribute("member", userDetails);
        model.addAttribute("moims", moims);
        return "home/loginhome";
    }



    /*Todo oauth 인증처리 폼로그인과 통합*/
    @GetMapping("/oauth/loginHome")
    public String oauthLoginHome(@AuthenticationPrincipal OAuth2User oAuth2User ,
                                 HttpServletRequest request,
                                 Model model) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2User.getAttribute("email"));
        Member member = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        model.addAttribute("member", member);
        List<Moim> moims = moimService.findAll();
        model.addAttribute("moims", moims);
        return "home/loginhome";
    }
}


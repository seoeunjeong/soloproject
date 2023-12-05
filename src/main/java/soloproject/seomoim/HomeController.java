package soloproject.seomoim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MoimService moimService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        String email = userDetails.getEmail();
        Long id = memberService.findByEmail(email).getId();
        List<Moim> moims = moimService.findAll();
        log.info("moims"+moims.get(0).getRegion());
//        List<Moim> flashedMoims = (List<Moim>) model.getAttribute("moims");
//
//        if (flashedMoims != null) {
//            moims = flashedMoims;
//        }

        model.addAttribute("memberId", id);
        model.addAttribute("moims", moims);
        return "home/home";
    }



    /*Todo oauth 인증객체 폼로그인객체와 통합*/
    @GetMapping("/oauth/loginHome")
    public String oauthLoginHome(@AuthenticationPrincipal OAuth2User oAuth2User,
                                 Model model) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2User.getAttribute("email"));
        Member member = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        model.addAttribute("member", member);
        List<Moim> moims = moimService.findAll();
        model.addAttribute("moims", moims);
        return "home/loginHome";
    }

    @GetMapping("/profile")
    public String profileFrom(@AuthenticationPrincipal CustomUserDetails userDetails,Model model){
        Member member = memberService.findByEmail(userDetails.getEmail());
        model.addAttribute("member",member);
        List<Moim> all = moimService.findAll();
        model.addAttribute("moims",all);

        return "home/profileHome";
    }

    @GetMapping("/alarm")
    public String alarmFrom(){
        return "home/alarmHome";
    }

    @GetMapping("/search")
    public String searchFrom(){
        return "home/searchHome";
    }
}


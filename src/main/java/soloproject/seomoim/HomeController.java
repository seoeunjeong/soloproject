package soloproject.seomoim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.security.FormLogin.CustomUserDetails;
import soloproject.seomoim.utils.PageResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MoimService moimService;
    private final MemberService memberService;
    private final MemberMapper mapper;
    private final MoimMapper moimMapper;


    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model, HttpServletRequest request) {
        String email = userDetails.getEmail();
        Long id = memberService.findByEmail(email).getId();
        Page<Moim> allbyPage = moimService.findAllbyPage(0, 12);
        List<Moim> moims = allbyPage.getContent();
        PageResponseDto<MoimDto.Response> responseDto = new PageResponseDto<>(moimMapper.moimsToResponseDtos(moims), allbyPage);
        List<Moim> popularMoims = moimService.findPopularMoims();
        List<Moim> todayMoims = moimService.findTodayMoims();
        List<MoimDto.Response> todayResponse = moimMapper.moimsToResponseDtos(todayMoims);
        List<MoimDto.Response> popularResponse = moimMapper.moimsToResponseDtos(popularMoims);
        model.addAttribute("memberId", id);
        model.addAttribute("moims", responseDto);
        model.addAttribute("todayMoims",todayResponse);
        model.addAttribute("popularMoims",popularResponse);
        return "home/home";
    }


    @GetMapping("/profile")
    public String profileFrom(@AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        Member member = memberService.findByEmail(userDetails.getEmail());
        model.addAttribute("member", mapper.memberToMemberResponseDto(member));

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


//    /*Todo oauth 인증객체 폼로그인객체와 통합*/
//    @GetMapping("/oauth/loginHome")
//    public String oauthLoginHome(@AuthenticationPrincipal OAuth2User oAuth2User,
//                                 Model model) {
//        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2User.getAttribute("email"));
//        Member member = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//        model.addAttribute("member", member);
//        List<Moim> moims = moimService.findAll();
//        model.addAttribute("moims", moims);
//        return "home/loginHome";
//    }
}


package soloproject.seomoim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import soloproject.seomoim.chat.ChatRoom;
import soloproject.seomoim.chat.ChatRoomRepository;
import soloproject.seomoim.member.loginCheck.Login;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.service.LatestViewService;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.utils.PageResponseDto;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MoimService moimService;
    private final MemberService memberService;
    private final MemberMapper mapper;
    private final MoimMapper moimMapper;
    private final ChatRoomRepository chatClassRepository;
    private final LatestViewService latestViewService;

    @GetMapping("/")
    public String home(@Login String email, Model model) {

        Long id = memberService.findByEmail(email).getId();
        model.addAttribute("memberId", id);

        Page<Moim> allbyPage = moimService.findAllByPage(0, 12);
        List<Moim> moims = allbyPage.getContent();
        PageResponseDto<MoimDto.Response> responseDto = new PageResponseDto<>(moimMapper.moimsToResponseDtos(moims), allbyPage);
        List<Moim> popularMoims = moimService.findPopularMoims();
        List<Moim> todayMoims = moimService.findTodayMoims();
        List<MoimDto.Response> todayResponse = moimMapper.moimsToResponseDtos(todayMoims);
        List<MoimDto.Response> popularResponse = moimMapper.moimsToResponseDtos(popularMoims);
        model.addAttribute("moims", responseDto);
        model.addAttribute("todayMoims",todayResponse);
        model.addAttribute("popularMoims",popularResponse);
        return "home/home";
    }


    @GetMapping("/profile")
    public String profileFrom(@Login String email,Model model) {
        Member member = memberService.findByEmail(email);
        model.addAttribute("member", mapper.memberToMemberResponseDto(member));
        Set<Object> latestView = latestViewService.getLatestPostsForMember(member.getId(), 5);
        model.addAttribute("latest",latestView);
        return "home/profile";
    }

    @GetMapping("/alarm")
    public String alarmFrom(@Login String mail,Model model){
        Member loginMember = memberService.findByEmail(mail);
        model.addAttribute("memberId",loginMember.getId());

        List<ChatRoom> chatRooms = loginMember.getChatRooms();
        List<Long> chatRoomIds = chatRooms.stream().map(ChatRoom::getId)
                .collect(Collectors.toList());

        model.addAttribute("chatRoomIds", chatRoomIds);
        model.addAttribute("roomId",chatRooms);
        return "home/alarm";
    }

    @GetMapping("/search")
    public String searchFrom(){
        return "home/search";
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


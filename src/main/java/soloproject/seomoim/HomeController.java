package soloproject.seomoim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import soloproject.seomoim.chat.message.ChatMessage;
import soloproject.seomoim.chat.message.ChatService;
import soloproject.seomoim.chat.room.ChatRoom;
import soloproject.seomoim.chat.room.ChatRoomRepository;
import soloproject.seomoim.member.loginCheck.AuthenticationUser;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.service.LatestViewService;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.moim.service.DistanceService;
import soloproject.seomoim.utils.PageResponseDto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final ChatRoomRepository chatRoomRepository;
    private final LatestViewService latestViewService;
    private final DistanceService distanceService;
    private final ChatService chatService;
    @GetMapping("/")
    public String home(Model model) {

        Page<Moim> totalPage = moimService.findAllByPage(0, 9);
        List<Moim> moims = totalPage.getContent();
        PageResponseDto<MoimDto.Response> totalResponse = new PageResponseDto<>(moimMapper.moimsToResponseDtos(moims), totalPage);

        List<Moim> popularMoims = moimService.findPopularMoims();
        List<Moim> todayMoims = moimService.findTodayMoims();

        List<MoimDto.Response> todayResponse = moimMapper.moimsToResponseDtos(todayMoims);
        List<MoimDto.Response> popularResponse = moimMapper.moimsToResponseDtos(popularMoims);

        model.addAttribute("moims", totalResponse);
        model.addAttribute("todayMoims", todayResponse);
        model.addAttribute("popularMoims", popularResponse);

        return "home/home";
    }


    @GetMapping("/profile")
    public String profileHome(@AuthenticationUser String email, Model model) {
        Member member = memberService.findMemberByEmail(email);
        model.addAttribute("member", mapper.memberToMemberResponseDto(member));
        Set<Object> latestViewMoim = latestViewService.getLatestPostsForMember(member.getId(), 5);
        model.addAttribute("latest", latestViewMoim);
        List<Moim> nearbyMoims = distanceService.findNearbyMoims(member);
        model.addAttribute("nearByMoims", nearbyMoims);
        return "home/profile";
    }

    @GetMapping("/chat")
    public String alarmHome(@AuthenticationUser String mail, Model model) {
        Member loginMember = memberService.findMemberByEmail(mail);
        List<ChatRoom> allChatRoom = chatRoomRepository.findByMember(loginMember);

        Map<Long, Long> unreadMessageCountMap = new HashMap<>();
        for (ChatRoom chatRoom : allChatRoom) {
            List<ChatMessage> unreadMessages = chatService.findUnreadMessageByLoginMember(loginMember,chatRoom.getId());
            long unreadMessageCount = unreadMessages.size();
            unreadMessageCountMap.put(chatRoom.getId(), unreadMessageCount);
        }

        List<Long> chatRoomIds = allChatRoom.stream().map(ChatRoom::getId)
                .collect(Collectors.toList());

        model.addAttribute("chatRooms",allChatRoom);
        model.addAttribute("unreadMessageCounts", unreadMessageCountMap);
        model.addAttribute("chatRoomIds", chatRoomIds);

        return "home/chat";
    }

    @GetMapping("/search")
    public String searchFrom(Model model){
        MoimSearchDto moimSearchDto = new MoimSearchDto();
        model.addAttribute("moimSearchDto",moimSearchDto);
        return "home/search";
    }


    @ModelAttribute("loginMemberId")
    public Long loginMember(@AuthenticationUser String email){
        Member loginMember = memberService.findMemberByEmail(email);
        return loginMember.getId();
    }
}


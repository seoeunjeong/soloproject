package soloproject.seomoim.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.loginCheck.Login;
import soloproject.seomoim.member.service.MemberService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chatRoom")
public class ChatRoomController {

     private final ChatRoomRepository chatRoomRepository;
     private final MemberService memberService;

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    // 채팅방 입장 화면
    @GetMapping("/chat-form/{room-id}")
    public String chatRoom(Model model, @PathVariable("room-id")String roomId,
                           @Login String email) {
        Member loginMember = memberService.findByEmail(email);
        model.addAttribute("roomId", roomId);
        model.addAttribute("loginMember",loginMember);
        return "moims/chat";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}

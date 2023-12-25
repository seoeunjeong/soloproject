package soloproject.seomoim.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.loginCheck.Login;
import soloproject.seomoim.member.service.MemberService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;


    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity createRoom(@RequestParam("receiver") Long receiverId,
                                     @RequestParam("sender") Long senderId) {

        Member receiverMember = memberService.findMember(receiverId);
        Member senderMember = memberService.findMember(senderId);

        List<Member> membersList = Arrays.asList(receiverMember,senderMember);

        Optional<ChatRoom> findRoom = chatRoomRepository.findByMember(senderMember);

        ChatRoom chatClass = null;

        if (findRoom.isPresent()) {
            ChatRoom.Dto dto = new ChatRoom.Dto();
            dto.setRoomId(findRoom.get().getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        if(findRoom.isEmpty()){
            ChatRoom createChatRoom = new ChatRoom();
            createChatRoom.addMember(receiverMember);
            createChatRoom.addMember(senderMember);

            ChatRoom createdChatRoom = chatRoomRepository.save(createChatRoom);
            ChatRoom.Dto dto = new ChatRoom.Dto();
            dto.setRoomId(createdChatRoom.getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        };

        return null;
    }


    // 채팅방 입장 화면
    @GetMapping("/chat-form/{room-id}/{receiver-id}")
    public String chatRoom(@PathVariable("room-id") Long roomId,
                           @PathVariable("receiver-id")Long receiverId,
                           @Login String email,Model model) {
        Member loginMember = memberService.findByEmail(email);
        model.addAttribute("loginMemberId",loginMember.getId());
        model.addAttribute("roomId",roomId);
        model.addAttribute("receiverId",receiverId);
        Optional<ChatRoom> chatClass = chatRoomRepository.findById(roomId);
        List<ChatMessage> byChatClass = chatMessageRepository.findByChatRoom(chatClass.get());
        model.addAttribute("allChat",byChatClass);
        return "moims/chat";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable Long roomId) {
        return chatRoomRepository.findById(roomId).get();
    }

}

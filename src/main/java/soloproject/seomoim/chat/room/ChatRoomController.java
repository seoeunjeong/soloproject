package soloproject.seomoim.chat.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.chat.message.ChatMessage;
import soloproject.seomoim.chat.message.ChatMessageRepository;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.loginCheck.Login;
import soloproject.seomoim.member.service.MemberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<ChatRoom.Dto> createRoom(@RequestParam("receiver") Long ownerMemberId,
                                        @RequestParam("sender") Long requestMemberId) {

        Member ownerMember = memberService.findMember(ownerMemberId);
        Member requestMember = memberService.findMember(requestMemberId);
        if(ownerMember==requestMember){
            throw new IllegalStateException("본인에게 1:1대화는 불가능합니다.");
        }
        Optional<ChatRoom> findRoom = chatRoomRepository.findByOwnerMemberAndRequestMember(ownerMember,requestMember);


        if (findRoom.isPresent()) {
            ChatRoom.Dto dto = new ChatRoom.Dto();
            dto.setRoomId(findRoom.get().getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        if(findRoom.isEmpty()){
            ChatRoom createChatRoom = new ChatRoom();
            createChatRoom.setOwnerMember(ownerMember);
            createChatRoom.setRequestMember(requestMember);

            ChatRoom createdChatRoom = chatRoomRepository.save(createChatRoom);
            ChatRoom.Dto dto = new ChatRoom.Dto();
            dto.setRoomId(createdChatRoom.getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return null;
    }

    // 채팅방 입장 화면
    @GetMapping("/chat-form/{room-id}")
    public String chatRoom(@PathVariable("room-id") Long roomId,
                           @Login String email,Model model) {
        Member loginMember = memberService.findByEmail(email);
        model.addAttribute("loginMemberId",loginMember.getId());
        model.addAttribute("roomId",roomId);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        List<ChatMessage> allMessage = chatMessageRepository.findByChatRoom(chatRoom.get());
        List<Long> messageIds = allMessage.stream().map(chatMessage -> chatMessage.getId())
                .collect(Collectors.toList());
        chatMessageRepository.saveAll(allMessage);
        model.addAttribute("allChat",allMessage);
        model.addAttribute("massageIds",messageIds);
        return "/moims/chatRoom";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable Long roomId) {

        return chatRoomRepository.findById(roomId).get();
    }


}

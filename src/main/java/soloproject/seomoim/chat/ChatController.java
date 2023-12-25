package soloproject.seomoim.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;

import java.time.LocalDateTime;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberService memberService;

    @MessageMapping("/chatMessage")
    public void sendMessage(@RequestBody ChatMessageDto.Post message) {;
        Long roomId = message.getRoomId();
        ChatMessage chatMessage = new ChatMessage();
        Member receiverMember = memberService.findMember(message.getReceiver());
        chatMessage.setReceiver(receiverMember);
        Member senderMember = memberService.findMember(message.getSender());
        chatMessage.setSender(senderMember);

        ChatRoom findRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("존재하지않는 채팅방입니다"));

        chatMessage.setContent(message.getContent());
        chatMessage.setChatRoom(findRoom);
        chatMessageRepository.save(chatMessage);

        ChatMessageDto.Response response = new ChatMessageDto.Response();

        if(senderMember.getProfileImage()!=null){
        String profileImageUrl = senderMember.getProfileImage().getProfileImageUrl();
            response.setSenderProfileUrl(profileImageUrl);
        }
        response.setSenderName(senderMember.getName());
        response.setSenderId(senderMember.getId());
        response.setCreatedAt(message.getCreatedAt());
        response.setContent(message.getContent());
        response.setRoomId(findRoom.getId());

        messagingTemplate.convertAndSend("/sub/chatMessage/" +message.getRoomId(), response);
    }

}
package soloproject.seomoim.chat.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.chat.room.ChatRoom;
import soloproject.seomoim.chat.room.ChatRoomRepository;
import soloproject.seomoim.chat.room.ChatRoomService;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.loginCheck.AuthenticationdUser;
import soloproject.seomoim.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @MessageMapping("/chatMessage")
    @Transactional
    public void sendMessage(@RequestBody ChatMessageDto.Send message) {
        ChatMessage savedMessage = chatService.createMessage(message);
        ChatMessageDto.Response responseMessage = chatMessageMapper(savedMessage);
        Long unReadMessageCount = chatService.GetUnReadMessageCount(message.getRoomId());
        responseMessage.setUnReadCount(unReadMessageCount);
        messagingTemplate.convertAndSend("/sub/chatMessage/" + message.getRoomId(), responseMessage);
    }

    private ChatMessageDto.Response chatMessageMapper(ChatMessage chatMessage) {
        ChatMessageDto.Response responseDto = new ChatMessageDto.Response();
        responseDto.setMessageId(chatMessage.getId());
        responseDto.setSenderId(chatMessage.getSender().getId());
        responseDto.setSenderName(chatMessage.getSender().getName());
        responseDto.setContent(chatMessage.getContent());
        responseDto.setReadStatus(chatMessage.getReadStatus());
        responseDto.setCreatedAt(chatMessage.getCreatedAt());
        if (chatMessage.getSender() != null && chatMessage.getSender().getProfileImage() != null) {
            String profileImageUrl = chatMessage.getSender().getProfileImage().getProfileImageUrl();
            responseDto.setSenderProfileUrl(profileImageUrl);
        }
        responseDto.setRoomId(chatMessage.getChatRoom().getId());
        //읽지않은 메세지수를 응답으로 같이 보내주자.

        return responseDto;
    }


    @MessageMapping("/mark-as-read/{messageId}")
    public void markAsRead(@DestinationVariable Long messageId) {
        ChatMessage chatMessage = chatService.updateReadStatus(messageId);
        ReadStatusDto readStatusDto = new ReadStatusDto();
        readStatusDto.setMessageId(chatMessage.getId());
        readStatusDto.setReadStatus(chatMessage.getReadStatus());
        messagingTemplate.convertAndSend("/sub/check-read",readStatusDto);
    }

    @Getter
    @Setter
    private class ReadStatusDto{
        private Long messageId;
        private ReadStatus readStatus;

    }
}

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
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    private final ChatService chatService;

    @MessageMapping("/chatMessage")
    public void sendMessage(@RequestBody ChatMessageDto.Send message) {

        ChatMessage savedMessage = chatService.createMessage(message);

        ChatMessageDto.Response responseMessage = chatMessageMapper(savedMessage);

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
        return responseDto;
    }


//    @MessageMapping("/mark-as-read/{roomId}")
//    public void markAsRead(@DestinationVariable Long roomId,
//                           @Payload MarkAsReadRequest request) {
//        //방입장시에 받은 메세지들의 상태를 읽음으로 변경
//        List<Long> messageIds = request.getMessageIds();
//        for(Long chatMessageId : messageIds){
//            ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId)
//                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 메시지"));
//            chatMessage.setReadStatus(ReadStatus.READ);
//            chatMessageRepository.save(chatMessage);
//        }
////        messagingTemplate.convertAndSend("/sub/check-read",readStatusDto);
//    }

//    @Getter @Setter
//    private static class ReadStatusDto{
//        private List<Long> messageId;
//        private boolean readStatus;
//
//        public void add(Long messageId){
//            this.messageId.add(messageId);
//        }
//    }

//    @Setter@Getter
//    private static class MarkAsReadRequest {
//        private List<Long> messageIds;
//    }

//
//    @MessageMapping("/mark-as-read")
//    public void markAsRead(@Payload MarkAsReadRequest request) {
//        List<Long> messageIds = request.getMessageIds();
//
//        for(Long chatMessageId : messageIds){
//            ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId)
//                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 메시지"));
//            chatMessage.setReadStatus(ReadStatus.READ);
//            chatMessageRepository.save(chatMessage);
//        }
//        messagingTemplate.convertAndSend("/sub/check-read",true);
//    }
}
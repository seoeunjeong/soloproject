package soloproject.seomoim.chat.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chatMessage")
    public void saveAndSendMessage(@RequestBody ChatMessageDto.Send message) {
        ChatMessage savedMessage = chatService.saveMessage(message);
        Long unReadMessageCount = chatService.getUnReadMessageCount(message.getRoomId());
        ChatMessageDto.Response responseMessage = chatMessageMapper(savedMessage);

//      todo 채팅방에 필요한 정보를 Dto로 만들기 ex)채팅목록에서구독시 필요한 읽지않은 메세지수
        responseMessage.setUnReadCount(unReadMessageCount);

        messagingTemplate.convertAndSend("/sub/chatMessage/" + message.getRoomId(), responseMessage);
    }

    @MessageMapping("/mark-as-read/{messageId}")
    public void markAsRead(@DestinationVariable Long messageId) {
        ChatMessage chatMessage = chatService.updateReadStatus(messageId);

        ChatMessageDto.Response response = chatMessageMapper(chatMessage);

        messagingTemplate.convertAndSend("/sub/check-read",response);
    }

    private ChatMessageDto.Response chatMessageMapper(ChatMessage chatMessage) {
        ChatMessageDto.Response responseDto = new ChatMessageDto.Response();
        responseDto.setMessageId(chatMessage.getId());
        responseDto.setSenderId(chatMessage.getSender().getId());
        responseDto.setSenderName(chatMessage.getSender().getName());
        responseDto.setContent(chatMessage.getContent());
        responseDto.setReadStatus(chatMessage.getReadStatus());
        responseDto.setCreatedAt(chatMessage.getCreatedAt());
        if (chatMessage.getSender().getProfileImage() != null) {
            String profileImageUrl = chatMessage.getSender().getProfileImage().getProfileImageUrl();
            responseDto.setSenderProfileUrl(profileImageUrl);
        }
        responseDto.setRoomId(chatMessage.getChatRoom().getId());

        return responseDto;
    }

}

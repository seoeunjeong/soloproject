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
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberService memberService;

    @MessageMapping("/chatMessage")
    @Transactional
    public void sendMessage(@RequestBody ChatMessageDto.Post message) {
        ChatMessage chatMessage = new ChatMessage();
        Member senderMember = memberService.findMember(message.getSender());
        chatMessage.setSender(senderMember);

        ChatRoom findRoom = chatRoomRepository.findById(message.getRoomId())
                .orElseThrow(() -> new IllegalStateException("존재하지않는 채팅방입니다"));
        chatMessage.setChatRoom(findRoom);
        chatMessage.setContent(message.getContent());
        chatMessage.setReadStatus(false);
        ChatMessage saveMessage = chatMessageRepository.save(chatMessage);

        ChatMessageDto.Response response = new ChatMessageDto.Response();

        if(senderMember.getProfileImage()!=null){
        String profileImageUrl = senderMember.getProfileImage().getProfileImageUrl();
            response.setSenderProfileUrl(profileImageUrl);
        }
        response.setSenderName(senderMember.getName());
        response.setSenderId(senderMember.getId());
        response.setCreatedAt(LocalDateTime.now());
        response.setContent(message.getContent());
        response.setRoomId(findRoom.getId());
        response.setMessageId(saveMessage.getId());

        messagingTemplate.convertAndSend("/sub/chatMessage/"+message.getRoomId(),response);
    }


    @MessageMapping("/mark-as-read/{messageId}")
    public void markAsRead(@DestinationVariable Long messageId,
                           @Payload MarkAsReadRequest request) {
        List<Long> messageIds = request.getMessageIds();

        for(Long chatMessageId : messageIds){
            ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 메시지"));
            chatMessage.setReadStatus(true);
            chatMessageRepository.save(chatMessage);
        }
        ChatMessage chatMessage = chatMessageRepository.findById(messageId).orElseThrow(() -> new IllegalStateException("존재하지않는메시지"));
        chatMessage.setReadStatus(true);
        chatMessageRepository.save(chatMessage);
        ReadStatusDto readStatusDto = new ReadStatusDto();
        readStatusDto.setMessageId(messageId);
        readStatusDto.setReadStatus(true);

        messagingTemplate.convertAndSend("/sub/check-read",readStatusDto);
    }

    @Getter @Setter
    private static class ReadStatusDto{
        private Long messageId;
        private boolean readStatus;

    }

    @Setter@Getter
    private static class MarkAsReadRequest {
        private List<Long> messageIds;
    }


    @MessageMapping("/mark-as-read")
    public void markAsRead(@Payload MarkAsReadRequest request) {
        List<Long> messageIds = request.getMessageIds();

        for(Long chatMessageId : messageIds){
            ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 메시지"));
            chatMessage.setReadStatus(true);
            chatMessageRepository.save(chatMessage);
        }
        messagingTemplate.convertAndSend("/sub/check-read",true);
    }
}
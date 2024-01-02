package soloproject.seomoim.chat.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.chat.room.ChatRoom;
import soloproject.seomoim.chat.room.ChatRoomService;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @Transactional
    public ChatMessage createMessage(ChatMessageDto.Send message) {
        Member senderMember = memberService.findMember(message.getSender());
        ChatRoom room = chatRoomService.findChatRoomById(message.getRoomId());
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(senderMember);
        chatMessage.setChatRoom(room);
        chatMessage.setContent(message.getContent());
        chatMessage.setReadStatus(ReadStatus.UNREAD);

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        return savedMessage;
    }
}

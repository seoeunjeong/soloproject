package soloproject.seomoim.chat.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.chat.room.ChatRoom;
import soloproject.seomoim.chat.room.ChatRoomService;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;

import java.util.List;
import java.util.Optional;

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

    public List<ChatMessage> findUnreadMessageByLoginMember(Member member,Long roomId){
        ChatRoom findChatRoom = chatRoomService.findChatRoomById(roomId);
        return chatMessageRepository.findUnReadMessageByLoginMember(member,findChatRoom,ReadStatus.UNREAD);
    }

    public Long GetUnReadMessageCount(Long roomId){
        return chatMessageRepository.countUnreadMessagesByChatRoomId(roomId);
    }

    @Transactional
    public ChatMessage updateReadStatus(Long messageId){
        ChatMessage chatMessage = findById(messageId);
        chatMessage.setReadStatus(ReadStatus.READ);
        return chatMessage;
    }

    public ChatMessage findById(Long messageId){
         return chatMessageRepository.findById(messageId)
                .orElseThrow(()->new IllegalStateException("존재하지않는 메세지입니다"));

    }
}

package soloproject.seomoim.chat.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.chat.room.ChatRoom;
import soloproject.seomoim.chat.room.ChatRoomService;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @Transactional
    public ChatMessage saveMessage(ChatMessageDto.Send message) {
        Member senderMember = memberService.findMemberById(message.getSenderId());
        ChatRoom room = chatRoomService.findChatRoomById(message.getRoomId());
        ChatMessage createdMessage = ChatMessage.create(senderMember, message.getContent(), ReadStatus.UNREAD, room);
        return chatMessageRepository.save(createdMessage);
    }

    public List<ChatMessage> findUnreadMessageByLoginMember(Member member,Long roomId){
        ChatRoom findChatRoom = chatRoomService.findChatRoomById(roomId);
        return chatMessageRepository.findUnReadMessageByLoginMember(member,findChatRoom,ReadStatus.UNREAD);
    }

    public Long getUnReadMessageCount(Long roomId){
        return chatMessageRepository.unreadMessageCountOfChatRoomId(roomId);
    }

    @Transactional
    public ChatMessage updateReadStatus(Long messageId) {
        ChatMessage chatMessage = findById(messageId);
        chatMessage.setReadStatus(ReadStatus.READ);
        return chatMessage;
    }

    public ChatMessage findById(Long messageId){
         return chatMessageRepository.findById(messageId)
                .orElseThrow(()->new IllegalStateException("존재하지않는 메세지입니다"));

    }
}

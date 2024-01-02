package soloproject.seomoim.chat.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soloproject.seomoim.chat.room.ChatRoom;
import soloproject.seomoim.member.entity.Member;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage> findByChatRoom(ChatRoom ChatRoom);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.sender <> :member AND cm.chatRoom = :chatRoom AND cm.readStatus = :status")
    List<ChatMessage> findUnReadMessageByLoginMember(@Param("member") Member member,
                                                     @Param("chatRoom") ChatRoom chatRoom,
                                                     @Param("status") ReadStatus status);

    List<ChatMessage> findAllByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatRoom.id = :roomId AND cm.readStatus = 'UNREAD'")
    Long countUnreadMessagesByChatRoomId(@Param("roomId") Long roomId);

}
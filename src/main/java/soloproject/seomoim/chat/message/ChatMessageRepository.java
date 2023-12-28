package soloproject.seomoim.chat.message;

import org.springframework.data.jpa.repository.JpaRepository;
import soloproject.seomoim.chat.room.ChatRoom;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage> findByChatRoom(ChatRoom ChatRoom);
}

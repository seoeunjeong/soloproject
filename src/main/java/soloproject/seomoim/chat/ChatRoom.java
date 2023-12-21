package soloproject.seomoim.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Setter
@Getter
public class ChatRoom {
    private String roomId; //구독할것!
    private String name; //방이름 모임장

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name=name;
        return chatRoom;
    }
}

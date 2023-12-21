package soloproject.seomoim.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Message {

    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String content; // 메시지
    private LocalDateTime createAt;
}
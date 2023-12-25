package soloproject.seomoim.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ChatMessageDto {

    @Getter
    @Setter
    public static class Post{
        private Long roomId;
        private Long receiver;
        private Long sender;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    public static class Response{
        private Long roomId;
        private String senderProfileUrl;
        private String senderName;
        private Long senderId;
        private LocalDateTime createdAt;
        private String content;
    }
}

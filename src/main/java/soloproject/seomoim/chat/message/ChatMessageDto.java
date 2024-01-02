package soloproject.seomoim.chat.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ChatMessageDto {
    @Getter
    @Setter
    public static class Send{
        private Long roomId;
        private Long receiver;
        private Long sender;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    public static class Response{
        private Long messageId;
        private Long senderId;
        private String senderName;
        private String senderProfileUrl;
        private String content;
        private ReadStatus readStatus;
        private LocalDateTime createdAt;
    }
}

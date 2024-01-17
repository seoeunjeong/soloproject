package soloproject.seomoim.chat.message;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ReadStatus {
    READ("읽음"),UNREAD("읽지않음");

    @Getter
    @JsonValue
    private String status;

    ReadStatus(String status) {
        this.status = status;
    }
}

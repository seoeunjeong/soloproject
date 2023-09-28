package soloproject.seomoim.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ExceptionCode {

    MEMBER_NOT_FOUND(404,"회원을 찾을 수 없습니다"),
    ALREADY_EXISTS_ID(400,"이미 존재하는 ID 입니다."),
    NOT_EXISTS_MOIM(404,"존재하지않는 모임입니다.");

    private int status;
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

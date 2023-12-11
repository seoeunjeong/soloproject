package soloproject.seomoim.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ExceptionCode {

    MEMBER_NOT_FOUND(404,"회원을 찾을 수 없습니다"),
    ALREADY_EXISTS_ID(400,"이미 존재하는 ID 입니다."),
    NOT_EXISTS_MOIM(404,"존재하지않는 모임입니다."),
    ALREADY_JOIN_MOIM(404,"이미 참여한 모임입니다."),
    NOT_ALLOW(400,"아이디 또는 비밀번호가 일치하지 않습니다."),
    NOT_ACCESS(400,"로그인 후 이용 할 수 있습니다."),
    INVALID_REQUEST(400, "유효하지않은 요청입니다"),
    MESSAGE_FAIL(500,"인증 메세지 발송이 실패했습니다"),
    CERTIFICATION_FAIL(400,"이메일 인증이 실패했습니다"),

    PASSWORD_MISMATCH(400,"비밀번호 확인이 불일치합니다.");

    private int status;
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

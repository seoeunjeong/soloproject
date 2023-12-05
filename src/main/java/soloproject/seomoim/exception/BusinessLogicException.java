package soloproject.seomoim.exception;

import lombok.Getter;

//todo! advice 예외처리 정리
@Getter
public class BusinessLogicException extends RuntimeException{

    private ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}

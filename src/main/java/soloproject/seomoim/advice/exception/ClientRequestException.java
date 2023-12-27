package soloproject.seomoim.advice.exception;

public class ClientRequestException extends RuntimeException {
    private ExceptionCode exceptionCode;

    public ClientRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}

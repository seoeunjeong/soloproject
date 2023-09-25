package soloproject.seomoim.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        return response;
    }
    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getExceptionCode());
        return new ResponseEntity(errorResponse,HttpStatus.valueOf(errorResponse.getStatus()));
    }

}

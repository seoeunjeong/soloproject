package soloproject.seomoim.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ErrorResponse;
import soloproject.seomoim.exception.ExceptionCode;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(e.getBindingResult());

        return response;
    }

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getExceptionCode());
        return new ResponseEntity(errorResponse,HttpStatus.valueOf(errorResponse.getStatus()));
    }

    /*바인딩 실패 예외 api 처리 어떻게 해야할까*/
    @ExceptionHandler
    public ResponseEntity handleInvalidFormatException(InvalidFormatException e) {
        log.info("e.getMessage={}",e.getMessage());
        log.info("e.getValue()={}",e.getValue());
        log.info("e.getValue()={}",e.getTargetType());

        ErrorResponse.of(ExceptionCode.INVALID_REQUEST);

        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }



}

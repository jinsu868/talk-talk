package carrot.candy.app.common.error.handler;

import carrot.candy.app.common.error.code.ErrorCode;
import carrot.candy.app.common.error.dto.ErrorResponse;
import carrot.candy.app.common.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handle(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("code = {} message = {}" , errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalServerException(Exception e) {
        log.warn("message = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}

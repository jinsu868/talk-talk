package carrot.candy.app.common.error.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ImageErrorCode implements ErrorCode {

    IMAGE_BAD_REQUEST("0010", HttpStatus.BAD_REQUEST, "IMAGE EXTENTION NOT VALID");


    private String code;
    private HttpStatus httpStatus;
    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }
}

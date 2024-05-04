package carrot.candy.app.member.exception;

import carrot.candy.app.common.error.code.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("0001", HttpStatus.OK, "MEMBER NOT FOUND");

    private String code;
    private HttpStatus status;
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
        return status;
    }
}

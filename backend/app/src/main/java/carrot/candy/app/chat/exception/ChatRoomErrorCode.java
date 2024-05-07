package carrot.candy.app.chat.exception;

import carrot.candy.app.common.error.code.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ChatRoomErrorCode implements ErrorCode {

    CHAT_ROOM_NOT_FOUND("1001", HttpStatus.OK, "ChatRoom not found"),
    MEMBER_NOT_IN_CHAR_ROOM("1002", HttpStatus.BAD_REQUEST, "Member is not in Chat Room");

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

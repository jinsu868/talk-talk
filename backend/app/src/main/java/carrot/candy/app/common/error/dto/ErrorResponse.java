package carrot.candy.app.common.error.dto;

import carrot.candy.app.common.error.code.ErrorCode;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}

package carrot.candy.app.chat.dto.request;

public record MessageSendRequest(
        Long roomId,
        String content,
        Long senderId
) {
}

package carrot.candy.app.chat.dto;

public record MessageSendRequest(
        Long roomId,
        String content,
        Long senderId
) {
}

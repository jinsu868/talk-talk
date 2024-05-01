package carrot.candy.app.chat.dto;

public record ChatRoomCreateRequest(
        String name,
        Long visitorId
) {
}

package carrot.candy.app.chat.dto.request;

public record ChatRoomCreateRequest(
        String name,
        Long visitorId
) {
}

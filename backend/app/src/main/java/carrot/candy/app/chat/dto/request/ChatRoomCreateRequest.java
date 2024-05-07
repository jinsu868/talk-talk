package carrot.candy.app.chat.dto.request;

import java.util.List;

public record ChatRoomCreateRequest(
        String name,
        List<Long> partnerIds
) {
}

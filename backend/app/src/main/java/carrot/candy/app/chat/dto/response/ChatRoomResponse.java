package carrot.candy.app.chat.dto.response;

import java.util.List;

public record ChatRoomResponse(
        Long id,
        String name,
        String recentChatMessage,
        List<String> partnerImages
) {

    public static ChatRoomResponse of(
            Long id,
            String name,
            String recentChatMessage,
            List<String> partnerImages
    ) {

        return new ChatRoomResponse(
                id,
                name,
                recentChatMessage,
                partnerImages
        );
    }
}

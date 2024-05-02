package carrot.candy.app.chat.dto.response;

import carrot.candy.app.member.domain.Member;

public record ChatRoomResponse(
        Long id,
        String partnerName,
        String partnerImage
) {

    public static ChatRoomResponse of(Long id, Member partner) {
        return new ChatRoomResponse(
                id,
                partner.getName(),
                partner.getImage().getImageUrl()
        );
    }
}

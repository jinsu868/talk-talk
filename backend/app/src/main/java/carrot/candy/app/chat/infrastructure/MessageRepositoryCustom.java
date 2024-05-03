package carrot.candy.app.chat.infrastructure;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.dto.response.MessageResponse;
import carrot.candy.app.common.dto.SliceResponse;

public interface MessageRepositoryCustom {
    SliceResponse<MessageResponse> findAllByChatRoomOrderByIdDesc(int pageSize, String cursor, ChatRoom chatRoom);

}

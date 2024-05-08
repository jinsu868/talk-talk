package carrot.candy.app.chat.infrastructure;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findChatRoomWithPartners(Long id);

    boolean existsByMemberIdAndChatRoomId(Long memberId, Long chatRoomId);
}

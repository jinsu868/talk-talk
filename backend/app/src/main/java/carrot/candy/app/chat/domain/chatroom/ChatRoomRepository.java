package carrot.candy.app.chat.domain.chatroom;

import carrot.candy.app.chat.infrastructure.ChatRoomRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
}

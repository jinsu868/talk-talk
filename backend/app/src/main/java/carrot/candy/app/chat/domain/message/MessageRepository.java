package carrot.candy.app.chat.domain.message;

import carrot.candy.app.chat.infrastructure.MessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {
}

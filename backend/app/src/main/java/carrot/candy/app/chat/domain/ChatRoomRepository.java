package carrot.candy.app.chat.domain;

import carrot.candy.app.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByOwnerAndVisitor(Member owner, Member visitor);
}

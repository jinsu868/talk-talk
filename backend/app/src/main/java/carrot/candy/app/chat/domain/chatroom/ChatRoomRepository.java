package carrot.candy.app.chat.domain.chatroom;

import carrot.candy.app.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByOwnerAndVisitor(Member owner, Member visitor);

    @Query("select cr from  ChatRoom  cr join fetch cr.owner o join fetch cr.visitor where cr.id = :id")
    Optional<ChatRoom> findByIdWithOwnerAndVisitor(Long id);

}

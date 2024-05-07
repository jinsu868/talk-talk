package carrot.candy.app.chat.infrastructure;

import static carrot.candy.app.chat.domain.chatroom.QChatRoom.chatRoom;
import static carrot.candy.app.chat.domain.chatroom.QChatRoomMember.chatRoomMember;
import static carrot.candy.app.member.domain.QMember.member;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRoom> findChatRoomWithPartners(Long id) {
        return jpaQueryFactory.selectFrom(chatRoom)
                .join(chatRoom.chatRoomMembers, chatRoomMember).fetchJoin()
                .join(chatRoomMember.member, member).fetchJoin()
                .where(isManager(id).or(
                        member.id.eq(id).and(chatRoom.isActive.eq(true))))
                .fetch();
    }

    @Override
    public boolean existsByMemberId(Long id) {
        ChatRoom result = jpaQueryFactory.selectFrom(chatRoom)
                .join(chatRoom.chatRoomMembers, chatRoomMember)
                .join(chatRoomMember.member, member)
                .where(chatRoom.managerId.eq(id).or(member.id.eq(id)))
                .fetchFirst();

        return result != null;
    }

    private BooleanExpression isManager(Long id) {
        return chatRoom.managerId.eq(id);
    }
}

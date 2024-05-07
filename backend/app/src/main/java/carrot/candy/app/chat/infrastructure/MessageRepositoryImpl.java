package carrot.candy.app.chat.infrastructure;

import static carrot.candy.app.chat.domain.message.QMessage.message;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.dto.response.MessageResponse;
import carrot.candy.app.common.dto.SliceResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SliceResponse<MessageResponse> findAllByChatRoomOrderByIdDesc(
            int pageSize,
            String cursor,
            ChatRoom chatRoom
    ) {
        List<MessageResponse> result = jpaQueryFactory.select(Projections.constructor(
                        MessageResponse.class,
                        message.id,
                        message.content,
                        message.member.id
                ))
                .from(message)
                .where(isInRange(cursor), ChatRoomEq(chatRoom))
                .orderBy(message.id.desc())
                .limit(pageSize + 1)
                .fetch();
        return convertToSlice(result, pageSize);
    }

    @Override
    public String findMessageContentByChatRoom(ChatRoom chatRoom) {
        return jpaQueryFactory.select(message.content)
                .from(message)
                .where(message.chatRoom.eq(chatRoom))
                .orderBy(message.id.desc())
                .limit(1)
                .fetchOne();
    }

    private BooleanExpression ChatRoomEq(ChatRoom chatRoom) {
        return message.chatRoom.eq(chatRoom);
    }

    private BooleanExpression isInRange(String cursor) {
        if (cursor == null) {
            return null;
        }

        return message.id.lt(Long.valueOf(cursor));
    }

    private SliceResponse<MessageResponse> convertToSlice(
            List<MessageResponse> messageResponses,
            int pageSize
    ) {
        boolean hasNext = existsNextValue(messageResponses, pageSize);
        String cursor = null;
        if (hasNext) {
            deleteLastValue(messageResponses);
            cursor = createNextCursor(messageResponses);
        }
        return SliceResponse.of(messageResponses, hasNext, cursor);
    }

    private String createNextCursor(List<MessageResponse> messageResponses) {
        MessageResponse messageResponse = messageResponses.get(messageResponses.size() - 1);
        return String.valueOf(messageResponse.id());
    }

    private void deleteLastValue(List<MessageResponse> messageResponses) {
        messageResponses.remove(messageResponses.size() - 1);
    }

    private boolean existsNextValue(List<MessageResponse> messageResponses, int pageSize) {
        if (messageResponses.size() > pageSize) {
            return true;
        }
        return false;
    }
}

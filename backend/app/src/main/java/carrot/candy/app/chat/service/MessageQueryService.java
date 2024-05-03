package carrot.candy.app.chat.service;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.domain.message.MessageRepository;
import carrot.candy.app.chat.dto.response.MessageResponse;
import carrot.candy.app.common.dto.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageQueryService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public SliceResponse<MessageResponse> findAllByChatRoomId(
            AuthMember authMember,
            Long id,
            int pageSize,
            String cursor
    ) {
        ChatRoom chatRoom = findChatRoom(id);
        chatRoom.checkMemberIn(authMember.getId());
        return messageRepository.findAllByChatRoomOrderByIdDesc(pageSize, cursor, chatRoom);
    }

    private ChatRoom findChatRoom(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("chatRoom not found"));
    }
}

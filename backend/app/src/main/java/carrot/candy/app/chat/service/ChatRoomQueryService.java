package carrot.candy.app.chat.service;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.dto.response.ChatRoomResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomResponse> findAllChatRoom(AuthMember authMember) {
        Long memberId = authMember.getId();
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByOwnerOrVisitorId(memberId);
        return chatRooms.stream()
                .map(chatRoom -> {
                    if (chatRoom.getVisitor().getId() == memberId) {
                        return ChatRoomResponse.of(chatRoom.getId(), chatRoom.getOwner());
                    }
                    return ChatRoomResponse.of(chatRoom.getId(), chatRoom.getVisitor());
                })
                .toList();
    }
}

package carrot.candy.app.chat.service;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.domain.message.MessageRepository;
import carrot.candy.app.chat.dto.response.ChatRoomResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {

    private static final int MAX_CHAT_ROOM_PROFILE_IMAGE_COUNT = 4;

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public List<ChatRoomResponse> findAllChatRoom(AuthMember authMember) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomWithPartners(authMember.getId());
        return chatRooms.stream()
                .map(chatRoom -> {
                    List<ChatRoomMember> chatRoomMembers = chatRoom.getChatRoomMembers();
                    List<String> partnerImages = new ArrayList<>();
                    extractPartnerImage(chatRoomMembers, partnerImages);
                    String recentMessage = null;
                    if (chatRoom.isActive()) {
                        recentMessage = messageRepository.findMessageContentByChatRoom(chatRoom);
                    }

                    return ChatRoomResponse.of(
                            chatRoom.getId(),
                            chatRoom.getName(),
                            recentMessage,
                            partnerImages
                    );
                }).toList();
    }

    private void extractPartnerImage(List<ChatRoomMember> chatRoomMembers, List<String> partnerImages) {
        int index = 0;
        while (index < chatRoomMembers.size() && index < MAX_CHAT_ROOM_PROFILE_IMAGE_COUNT) {
            partnerImages.add(chatRoomMembers.get(index)
                            .getMember()
                            .getImage()
                            .getImageUrl());
            index++;
        }
    }
}

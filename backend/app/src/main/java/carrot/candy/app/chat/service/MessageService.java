package carrot.candy.app.chat.service;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.domain.message.Message;
import carrot.candy.app.chat.domain.message.MessageRepository;
import carrot.candy.app.chat.dto.MessageSendRequest;
import carrot.candy.app.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void save(MessageSendRequest request) {
        ChatRoom chatRoom = findChatRoomWithOwnerAndVisitor(request.roomId());
        chatRoom.checkMemberIn(request.senderId());
        Member sender = chatRoom.findSender(request.senderId());
        messageRepository.save(Message.createMessage(request.content(), sender, chatRoom));
    }

    private ChatRoom findChatRoomWithOwnerAndVisitor(Long id) {
        return chatRoomRepository.findByIdWithOwnerAndVisitor(id)
                .orElseThrow(() -> new IllegalArgumentException("chat room not found"));
    }
}

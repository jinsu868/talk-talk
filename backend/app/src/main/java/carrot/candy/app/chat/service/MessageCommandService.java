package carrot.candy.app.chat.service;

import static carrot.candy.app.chat.exception.ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND;
import static carrot.candy.app.chat.exception.ChatRoomErrorCode.MEMBER_NOT_IN_CHAR_ROOM;
import static carrot.candy.app.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.domain.message.Message;
import carrot.candy.app.chat.domain.message.MessageRepository;
import carrot.candy.app.chat.dto.request.MessageSendRequest;
import carrot.candy.app.common.error.exception.BusinessException;
import carrot.candy.app.member.domain.Member;
import carrot.candy.app.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageCommandService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(MessageSendRequest request) {
        ChatRoom chatRoom = findChatRoom(request.roomId());
        validateMemberInChatRoom(request.senderId(), request.roomId());
        Member sender = findMember(request.senderId());
        chatRoom.activate();
        messageRepository.save(Message.createMessage(request.content(), sender, chatRoom));
    }

    private void validateMemberInChatRoom(Long id, Long chatRoomId) {
        if (!chatRoomRepository.existsByMemberIdAndChatRoomId(id, chatRoomId)) {
            throw new BusinessException(MEMBER_NOT_IN_CHAR_ROOM);
        }
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    private ChatRoom findChatRoom(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CHAT_ROOM_NOT_FOUND));
    }
}

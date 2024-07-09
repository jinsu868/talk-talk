package carrot.candy.app.chat.service;

import static carrot.candy.app.chat.exception.ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND;
import static carrot.candy.app.chat.exception.ChatRoomErrorCode.MEMBER_NOT_IN_CHAR_ROOM;
import static carrot.candy.app.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.domain.message.Message;
import carrot.candy.app.chat.domain.message.MessageRepository;
import carrot.candy.app.chat.dto.request.MessageSendRequest;
import carrot.candy.app.chat.infrastructure.RedisSubscriber;
import carrot.candy.app.common.error.exception.BusinessException;
import carrot.candy.app.member.domain.Member;
import carrot.candy.app.member.domain.MemberRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageCommandService {

    private static final String CHANNEL_NAME_PREFIX = "chat-channel:";
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final Map<Long, ChannelTopic> topics = new ConcurrentHashMap<>();
    private final RedisSubscriber redisSubscriber;

    public void sendMessage(MessageSendRequest request) {
        ChatRoom chatRoom = findChatRoom(request.roomId());
        validateMemberInChatRoom(request.senderId());
        Member sender = findMember(request.senderId());
        chatRoom.activate();
        ChannelTopic topic = topics.get(getChannelName(chatRoom.getId()));
        if (topic == null) {
            topic = ChannelTopic.of(getChannelName(chatRoom.getId()));
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(chatRoom.getId(), topic);
        }

        redisTemplate.convertAndSend(getChannelName(chatRoom.getId()), request);
        messageRepository.save(Message.createMessage(request.content(), sender, chatRoom));
    }

    private void validateMemberInChatRoom(Long id) {
        if (!chatRoomRepository.existsByMemberId(id)) {
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

    private String getChannelName(Long chatRoomId) {
        return CHANNEL_NAME_PREFIX + chatRoomId;
    }
}

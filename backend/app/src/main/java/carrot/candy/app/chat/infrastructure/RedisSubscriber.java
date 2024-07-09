package carrot.candy.app.chat.infrastructure;

import carrot.candy.app.chat.dto.request.MessageSendRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations sendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String receivedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
        MessageSendRequest request;
        try {
             request = objectMapper.readValue(receivedMessage, MessageSendRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        sendingOperations.convertAndSend("/queue/chat-rooms/" + request.roomId(), request);
    }
}

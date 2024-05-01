package carrot.candy.app.chat.controller;

import carrot.candy.app.chat.dto.MessageSendRequest;
import carrot.candy.app.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/message")
    public void sendMessage(@Payload MessageSendRequest request) {
        log.info("chat message");
        sendingOperations.convertAndSend("/queue/chat-rooms/" + request.roomId(), request);
        messageService.save(request);
    }
}

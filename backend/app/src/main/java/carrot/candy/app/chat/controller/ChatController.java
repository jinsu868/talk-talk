package carrot.candy.app.chat.controller;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.dto.request.MessageSendRequest;
import carrot.candy.app.chat.dto.response.MessageResponse;
import carrot.candy.app.chat.service.MessageQueryService;
import carrot.candy.app.chat.service.MessageCommandService;
import carrot.candy.app.common.annotation.PreAuthorize;
import carrot.candy.app.common.dto.SliceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final MessageCommandService messageCommandService;
    private final MessageQueryService messageQueryService;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/message")
    public void sendMessage(@Payload MessageSendRequest request) {
        log.info("chat message");
        sendingOperations.convertAndSend("/queue/chat-rooms/" + request.roomId(), request);
        messageCommandService.save(request);
    }

    @PreAuthorize
    @GetMapping("/api/v1/messages/chat-rooms/{id}")
    public ResponseEntity<SliceResponse<MessageResponse>> find(
            AuthMember authMember,
            @PathVariable Long id,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(messageQueryService.findAllByChatRoomId(authMember, id, pageSize, cursor));
    }
}

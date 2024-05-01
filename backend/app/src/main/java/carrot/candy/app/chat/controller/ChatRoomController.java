package carrot.candy.app.chat.controller;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.dto.ChatRoomCreateRequest;
import carrot.candy.app.chat.service.ChatRoomService;
import carrot.candy.app.common.annotation.PreAuthorize;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    @PreAuthorize
    public ResponseEntity<Void> create(
            AuthMember authMember,
            @RequestBody ChatRoomCreateRequest request) {
        Long id = chatRoomService.createChatRoom(authMember, request);
        return ResponseEntity.created(URI.create("/api/v1/chat-rooms/" + id))
                .build();
    }
}

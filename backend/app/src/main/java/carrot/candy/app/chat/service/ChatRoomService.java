package carrot.candy.app.chat.service;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.dto.ChatRoomCreateRequest;
import carrot.candy.app.chat.dto.response.ChatRoomResponse;
import carrot.candy.app.member.domain.Member;
import carrot.candy.app.member.domain.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public Long createChatRoom(AuthMember authMember, ChatRoomCreateRequest request) {
        Member visitor = findMember(request.visitorId());
        Member owner = findMember(authMember.getId());
        validateCreateChatRoom(owner, visitor);
        return chatRoomRepository.save(ChatRoom.createChatRoom(request.name(), visitor, owner))
                .getId();
    }

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

    private void validateCreateChatRoom(Member owner, Member visitor) {
        if (chatRoomRepository.existsByOwnerAndVisitor(owner, visitor)) {
            throw new IllegalStateException("Already exists ChatRoom between owner and visitor");
        }
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
    }
}

package carrot.candy.app.chat.service;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoomMemberRepository;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.dto.request.ChatRoomCreateRequest;
import carrot.candy.app.member.domain.Member;
import carrot.candy.app.member.domain.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public Long createChatRoom(AuthMember authMember, ChatRoomCreateRequest request) {
        Member manager = findMember(authMember.getId());
        List<Member> partners = request.partnerIds().stream()
                .map(this::findMember)
                .toList();
        ChatRoom chatRoom = ChatRoom.createChatRoom(request.name(), manager.getId());
        chatRoomRepository.save(chatRoom);
        for (Member partner : partners) {
            chatRoomMemberRepository.save(ChatRoomMember.createChatRoomMember(chatRoom, partner));
        }

        return chatRoom.getId();
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
    }
}

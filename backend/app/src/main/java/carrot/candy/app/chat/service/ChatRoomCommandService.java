package carrot.candy.app.chat.service;

import static carrot.candy.app.chat.exception.ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND;
import static carrot.candy.app.chat.exception.ChatRoomErrorCode.MEMBER_NOT_IN_CHAR_ROOM;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.chat.domain.chatroom.ChatRoomMember;
import carrot.candy.app.chat.domain.chatroom.ChatRoomMemberRepository;
import carrot.candy.app.chat.domain.chatroom.ChatRoomRepository;
import carrot.candy.app.chat.dto.request.ChatRoomCreateRequest;
import carrot.candy.app.chat.dto.request.ChatRoomInviteRequest;
import carrot.candy.app.chat.exception.ChatRoomErrorCode;
import carrot.candy.app.common.error.exception.BusinessException;
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

    public void invite(AuthMember authMember, Long id, ChatRoomInviteRequest request) {
        ChatRoom chatRoom = findChatRoom(id);
        validateMemberInChatRoom(authMember, id);
        Member invitedMember = findMember(request.memberId());
        validateDuplicatedEnterChatRoom(invitedMember, chatRoom);
        chatRoomMemberRepository.save(ChatRoomMember.createChatRoomMember(chatRoom, invitedMember));
    }

    private void validateDuplicatedEnterChatRoom(Member invitedMember, ChatRoom chatRoom) throws BusinessException {
        if (chatRoomRepository.existsByMemberIdAndChatRoomId(invitedMember.getId(), chatRoom.getId())) {
            throw new BusinessException(ChatRoomErrorCode.ALREADY_PARTICIPATE_CHAT_ROOM);
        }
    }

    private void validateMemberInChatRoom(AuthMember authMember, Long id) {
        if (!chatRoomRepository.existsByMemberIdAndChatRoomId(authMember.getId(), id)) {
            throw new BusinessException(MEMBER_NOT_IN_CHAR_ROOM);
        }
    }

    private ChatRoom findChatRoom(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CHAT_ROOM_NOT_FOUND));
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
    }
}

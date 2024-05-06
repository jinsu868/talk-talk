package carrot.candy.app.chat.domain.message;

import carrot.candy.app.chat.domain.chatroom.ChatRoom;
import carrot.candy.app.common.entity.BaseTimeEntity;
import carrot.candy.app.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4098)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public static Message createMessage(
            String content,
            Member member,
            ChatRoom chatRoom
    ) {
        return new Message(
                content,
                member,
                chatRoom
        );
    }

    private Message(
            String content,
            Member member,
            ChatRoom chatRoom
    ) {
        this.content = content;
        this.member = member;
        this.chatRoom = chatRoom;
    }
}

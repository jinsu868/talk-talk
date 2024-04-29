package carrot.candy.app.chat.domain;

import carrot.candy.app.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id")
    private Member visitor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    public static ChatRoom createChatRoom(
            String name,
            Member visitor,
            Member owner
    ) {
        return new ChatRoom(
                name,
                visitor,
                owner
        );
    }

    private ChatRoom(
            String name,
            Member visitor,
            Member owner
    ) {
        this.name = name;
        this.visitor = visitor;
        this.owner = owner;
    }
}

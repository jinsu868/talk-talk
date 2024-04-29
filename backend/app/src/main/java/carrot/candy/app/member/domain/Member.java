package carrot.candy.app.member.domain;

import carrot.candy.app.common.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 250)
    private String description;

    @Column(nullable = false)
    private Image image;

    private Member(
            String name,
            String description,
            String imageUrl
    ) {
        this.name = name;
        this.description = description;
        this.image = Image.createImage(imageUrl);
    }

    public static Member createMember(
            String name,
            String description,
            String imageUrl
    ) {
        return new Member(
                name,
                description,
                imageUrl
        );
    }
}

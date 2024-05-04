package carrot.candy.app.member.domain;

import static carrot.candy.app.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

import carrot.candy.app.common.entity.Image;
import carrot.candy.app.common.error.exception.BusinessException;
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    private Member(
            String name,
            String description,
            String imageUrl,
            String email,
            String password
    ) {
        this.name = name;
        this.description = description;
        this.image = Image.createImage(imageUrl);
        this.email = email;
        this.password = password;
    }

    public static Member createMember(
            String name,
            String description,
            String imageUrl,
            String email,
            String password
    ) {
        return new Member(
                name,
                description,
                imageUrl,
                email,
                password
        );
    }

    public void login(String password) {
        if (!this.password.equals(password)) {
            throw new BusinessException(MEMBER_NOT_FOUND);
        }
    }
}

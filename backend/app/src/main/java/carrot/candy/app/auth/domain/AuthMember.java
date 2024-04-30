package carrot.candy.app.auth.domain;

import lombok.Getter;

@Getter
public class AuthMember {

    private Long id;

    private AuthMember(Long id) {
        this.id = id;
    }
    public static AuthMember createAuthMember(Long id) {
        return new AuthMember(id);
    }
}

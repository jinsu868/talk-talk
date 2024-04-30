package carrot.candy.app.auth.infrastructure;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenExtractor {

    private static final String BEARER = "Bearer ";

    public String extractAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && isBearerToken(accessToken)) {
            return accessToken.substring(7);
        }
        return null;
    }

    private boolean isBearerToken(String accessToken) {
        return accessToken.toLowerCase()
                .startsWith(BEARER.toLowerCase());
    }
}

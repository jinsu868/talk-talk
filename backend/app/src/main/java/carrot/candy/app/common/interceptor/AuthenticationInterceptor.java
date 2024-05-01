package carrot.candy.app.common.interceptor;

import carrot.candy.app.auth.infrastructure.TokenExtractor;
import carrot.candy.app.auth.infrastructure.TokenProvider;
import carrot.candy.app.auth.service.AuthService;
import carrot.candy.app.common.annotation.PreAuthorize;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenExtractor tokenExtractor;
    private final TokenProvider tokenProvider;
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (!isRequiredAuth((HandlerMethod) handler)) {
            return true;
        }

        Long memberId = extractMemberIdFromRequest(request);
        authService.validateAuthMember(memberId);
        request.setAttribute("memberId", memberId);
        return true;
    }

    private boolean isRequiredAuth(HandlerMethod handler) {
        PreAuthorize preAuthorize = handler.getMethodAnnotation(PreAuthorize.class);
        return !Objects.isNull(preAuthorize);
    }

    private Long extractMemberIdFromRequest(HttpServletRequest request) {
        String accessToken = tokenExtractor.extractAccessToken(request);
        String memberId = tokenProvider.getPayload(accessToken);
        if (StringUtils.hasText(memberId)) {
            return Long.parseLong(memberId);
        }

        return null;
    }
}

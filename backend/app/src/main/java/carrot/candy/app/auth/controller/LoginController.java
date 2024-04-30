package carrot.candy.app.auth.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import carrot.candy.app.auth.dto.LoginRequest;
import carrot.candy.app.auth.dto.SignUpRequest;
import carrot.candy.app.auth.infrastructure.TokenProvider;
import carrot.candy.app.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private static final String AUTH_PREFIX = "Bearer ";
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response, @RequestBody LoginRequest request) {
        Long id = authService.login(request);
        response.setHeader(AUTHORIZATION, tokenProvider.createAccessToken(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(HttpServletResponse response, @RequestBody SignUpRequest request) {
        Long id = authService.signUp(request);
        response.setHeader(AUTHORIZATION, tokenProvider.createAccessToken(id));
        return ResponseEntity.created(URI.create("/api/v1/sign-up/" + id))
                .build();
    }
}

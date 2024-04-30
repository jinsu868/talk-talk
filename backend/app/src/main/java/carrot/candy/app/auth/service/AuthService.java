package carrot.candy.app.auth.service;

import carrot.candy.app.auth.domain.AuthMember;
import carrot.candy.app.auth.dto.LoginRequest;
import carrot.candy.app.auth.dto.SignUpRequest;
import carrot.candy.app.common.util.PasswordUtil;
import carrot.candy.app.member.domain.Member;
import carrot.candy.app.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;

    public void validateAuthMember(Long id) {
        if (id == null || !memberRepository.existsById(id)) {
            throw new IllegalArgumentException("Fail Authentication");
        }
    }

    public AuthMember findByMemberId(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        return AuthMember.createAuthMember(member.getId());
    }

    public Long login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        String encryptedPassword = passwordUtil.encrypt(request.email(), request.password());
        member.login(encryptedPassword);
        return member.getId();
    }

    @Transactional
    public Long signUp(SignUpRequest request) {
        validateCreateMember(request);
        String encryptedPassword = passwordUtil.encrypt(request.email(), request.password());
        Member member = Member.createMember(
                request.name(),
                request.description(),
                request.imageUrl(),
                request.email(),
                encryptedPassword
        );
        memberRepository.save(member);
        return member.getId();
    }

    private void validateCreateMember(SignUpRequest request) {
        validateDuplicateEmail(request.email());
        validateDuplicateName(request.name());
    }

    private void validateDuplicateName(String name) {
        if (memberRepository.existsByName(name)) {
            throw new IllegalArgumentException("Already exists name");
        }
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Already exists account");
        }
    }
}

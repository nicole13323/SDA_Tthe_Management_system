package com.sdatithe.service;

import com.sdatithe.dto.Dtos.AuthResponse;
import com.sdatithe.dto.Dtos.LoginRequest;
import com.sdatithe.model.Church;
import com.sdatithe.model.Member;
import com.sdatithe.model.Role;
import com.sdatithe.model.TitheManager;
import com.sdatithe.repo.ChurchRepository;
import com.sdatithe.repo.ManagerRepository;
import com.sdatithe.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Resolves the account from a single identifier:
 *  - Tithe Manager : username
 *  - Church        : name or church ID (code)
 *  - Member        : phone number or email
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final ManagerRepository managers;
    private final ChurchRepository churches;
    private final MemberRepository members;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final AuditService audit;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String id = req.identifier().trim();
        String pw = req.password();

        Optional<TitheManager> mgr = managers.findByUsername(id);
        if (mgr.isPresent() && encoder.matches(pw, mgr.get().getPasswordHash())) {
            TitheManager m = mgr.get();
            audit.log(m.getUsername(), Role.TITHE_MANAGER.name(), "LOGIN", "");
            return new AuthResponse(jwt.issue(Role.TITHE_MANAGER, m.getId(), m.getUsername(), null),
                    Role.TITHE_MANAGER.name(), m.getId(), m.getUsername(), null, null, jwt.ttlSeconds());
        }

        Optional<Church> church = churches.findFirstByCodeIgnoreCaseOrNameIgnoreCase(id, id);
        if (church.isPresent() && encoder.matches(pw, church.get().getPasswordHash())) {
            Church c = church.get();
            audit.log(c.getCode(), Role.CHURCH.name(), "LOGIN", c.getName());
            return new AuthResponse(jwt.issue(Role.CHURCH, c.getId(), c.getName(), c.getId()),
                    Role.CHURCH.name(), c.getId(), c.getName(), c.getId(), null, jwt.ttlSeconds());
        }

        Optional<Member> member = members.findByPhone(PhoneUtil.normalize(id))
                .or(() -> members.findByEmailIgnoreCase(id));
        if (member.isPresent() && encoder.matches(pw, member.get().getPasswordHash())) {
            Member m = member.get();
            Long churchId = m.getChurch().getId();
            audit.log(m.getPhone(), Role.MEMBER.name(), "LOGIN", "");
            return new AuthResponse(jwt.issue(Role.MEMBER, m.getId(), m.getFullName(), churchId),
                    Role.MEMBER.name(), m.getId(), m.getFullName(), churchId, m.getPreferredLanguage(), jwt.ttlSeconds());
        }

        throw new BadCredentialsException("Invalid identifier or password");
    }
}

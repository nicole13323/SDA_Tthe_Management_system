package com.sdatithe.service;

import com.sdatithe.dto.Dtos.*;
import com.sdatithe.model.Church;
import com.sdatithe.model.Member;
import com.sdatithe.model.Role;
import com.sdatithe.repo.ChurchRepository;
import com.sdatithe.repo.MemberRepository;
import com.sdatithe.web.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChurchService {
    private static final Set<String> LANGS = Set.of("rw", "sw", "en", "fr");
    public static final String DEFAULT_PASSWORD = "1234";

    private final ChurchRepository churches;
    private final MemberRepository members;
    private final PasswordEncoder encoder;
    private final AuditService audit;

    @Transactional(readOnly = true)
    public List<MemberDto> members(Long churchId) {
        return members.findByChurch_IdOrderByFullNameAsc(churchId).stream().map(Mappers::member).toList();
    }

    @Transactional
    public MemberDto addMember(Long churchId, AddMemberRequest req) {
        Church church = churches.findById(churchId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Church not found"));
        String phone = PhoneUtil.normalize(req.phone());
        if (!PhoneUtil.isValid(phone)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Rwandan phone number (expected 07XXXXXXXX)");
        }
        if (members.existsByPhone(phone)) throw new ApiException(HttpStatus.CONFLICT, "Phone number already registered");
        String email = req.email() == null || req.email().isBlank() ? null : req.email().trim().toLowerCase();
        if (email != null && members.existsByEmailIgnoreCase(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
        }
        Member m = new Member();
        m.setChurch(church);
        m.setFullName(req.fullName().trim());
        m.setPhone(phone);
        m.setEmail(email);
        m.setPasswordHash(encoder.encode(DEFAULT_PASSWORD));
        m.setPreferredLanguage(req.preferredLanguage() != null && LANGS.contains(req.preferredLanguage()) ? req.preferredLanguage() : "rw");
        members.save(m);
        audit.log(church.getCode(), Role.CHURCH.name(), "ADD_MEMBER", "member#" + m.getId());
        return Mappers.member(m);
    }

    @Transactional(readOnly = true)
    public List<MemberDto> search(String q) {
        if (q == null || q.isBlank()) return List.of();
        return members.search(q.trim(), PageRequest.of(0, 20)).stream().map(Mappers::member).toList();
    }

    @Transactional(readOnly = true)
    public List<ChurchDto> listChurches() {
        return churches.findAll().stream().map(Mappers::church).toList();
    }
}

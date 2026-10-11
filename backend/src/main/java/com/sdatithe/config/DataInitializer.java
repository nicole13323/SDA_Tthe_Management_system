package com.sdatithe.config;

import com.sdatithe.model.*;
import com.sdatithe.repo.*;
import com.sdatithe.service.ReceiptNumbers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Demo data. All demo accounts use the password 1234 (stored BCrypt-hashed).
 *   Tithe Manager : manager
 *   Churches      : CH001, CH002, CH003 (or their names)
 *   Members       : 0788000001 ... 0788000012 (or member1@example.rw ...)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final ManagerRepository managers;
    private final ChurchRepository churches;
    private final MemberRepository members;
    private final PaymentRepository payments;
    private final ReceiptRepository receipts;
    private final PasswordEncoder encoder;
    @Value("${app.seed:true}") private boolean seed;

    @Override
    public void run(String... args) {
        if (!seed || managers.count() > 0) return;
        String hash = encoder.encode("1234");

        TitheManager manager = new TitheManager();
        manager.setUsername("manager");
        manager.setPasswordHash(hash);
        managers.save(manager);

        String[][] churchData = {
                {"CH001", "Kigali Central SDA Church", "Gasabo", "Kimihurura"},
                {"CH002", "Nyamirambo SDA Church", "Nyarugenge", "Nyamirambo"},
                {"CH003", "Huye SDA Church", "Huye", "Ngoma"}};
        String[] names = {"Jean Claude Habimana", "Marie Uwase", "Eric Nshimiyimana", "Alice Mukamana",
                "Patrick Niyonzima", "Grace Ingabire", "Emmanuel Hakizimana", "Divine Uwimana",
                "Samuel Ndayisaba", "Josiane Mukashema", "David Tuyishime", "Claudine Umutoni"};
        String[] langs = {"rw", "en", "fr", "sw"};

        List<Member> all = new ArrayList<>();
        int n = 0;
        for (String[] c : churchData) {
            Church church = new Church();
            church.setCode(c[0]); church.setName(c[1]); church.setDistrict(c[2]); church.setSector(c[3]);
            church.setPasswordHash(hash);
            churches.save(church);
            for (int i = 0; i < 4; i++, n++) {
                Member m = new Member();
                m.setChurch(church);
                m.setFullName(names[n]);
                m.setPhone(String.format("07880000%02d", n + 1));
                m.setEmail("member" + (n + 1) + "@example.rw");
                m.setPasswordHash(hash);
                m.setPreferredLanguage(langs[n % 4]);
                all.add(members.save(m));
            }
        }

        // Historical payments so that weekly / monthly / yearly reports have data.
        Random rnd = new Random(2026);
        PaymentMethod[] methods = PaymentMethod.values();
        int[] amounts = {5000, 10000, 15000, 20000, 25000, 50000};
        for (Member m : all) {
            for (int i = 0; i < 8; i++) {
                PaymentMethod method = methods[rnd.nextInt(methods.length)];
                Payment p = new Payment();
                p.setMember(m);
                p.setChurch(m.getChurch());
                p.setAmount(BigDecimal.valueOf(amounts[rnd.nextInt(amounts.length)]));
                p.setMethod(method);
                p.setStatus(PaymentStatus.SUCCESS);
                p.setReference("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                p.setRecordedBy(method == PaymentMethod.CASH ? manager : null);
                p.setPaidAt(Instant.now().minus(rnd.nextInt(400), ChronoUnit.DAYS).minus(rnd.nextInt(600), ChronoUnit.MINUTES));
                payments.save(p);
                Receipt r = new Receipt();
                r.setPayment(p);
                r.setReceiptNo(ReceiptNumbers.next());
                r.setIssuedAt(p.getPaidAt());
                receipts.save(r);
            }
        }
        log.info("Demo data created: 1 manager, {} churches, {} members", churchData.length, all.size());
    }
}

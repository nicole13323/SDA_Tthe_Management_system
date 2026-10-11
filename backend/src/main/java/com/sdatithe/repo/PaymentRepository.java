package com.sdatithe.repo;

import com.sdatithe.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @EntityGraph(attributePaths = {"member", "church"})
    List<Payment> findByMember_IdOrderByPaidAtDesc(Long memberId);

    @EntityGraph(attributePaths = {"member", "church"})
    @Query("select p from Payment p where p.church.id = :churchId and p.paidAt >= :from and p.paidAt < :to order by p.paidAt desc")
    Page<Payment> findChurchPeriod(@Param("churchId") Long churchId, @Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "church"})
    @Query("select p from Payment p where p.paidAt >= :from and p.paidAt < :to order by p.paidAt desc")
    Page<Payment> findAllPeriod(@Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @Query("select sum(p.amount) from Payment p where p.church.id = :churchId and p.paidAt >= :from and p.paidAt < :to and p.status = com.sdatithe.model.PaymentStatus.SUCCESS")
    BigDecimal sumChurchPeriod(@Param("churchId") Long churchId, @Param("from") Instant from, @Param("to") Instant to);

    @Query("select sum(p.amount) from Payment p where p.paidAt >= :from and p.paidAt < :to and p.status = com.sdatithe.model.PaymentStatus.SUCCESS")
    BigDecimal sumAllPeriod(@Param("from") Instant from, @Param("to") Instant to);
}

package com.sdatithe.repo;

import com.sdatithe.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByReceiptNo(String receiptNo);

    @Query("select r from Receipt r join fetch r.payment where r.payment.id in :ids")
    List<Receipt> findByPaymentIds(@Param("ids") Collection<Long> ids);
}

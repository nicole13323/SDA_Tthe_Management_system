package com.sdatithe.repo;

import com.sdatithe.model.Church;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChurchRepository extends JpaRepository<Church, Long> {
    Optional<Church> findFirstByCodeIgnoreCaseOrNameIgnoreCase(String code, String name);
}

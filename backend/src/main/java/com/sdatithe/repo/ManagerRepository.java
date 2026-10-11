package com.sdatithe.repo;

import com.sdatithe.model.TitheManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<TitheManager, Long> {
    Optional<TitheManager> findByUsername(String username);
}

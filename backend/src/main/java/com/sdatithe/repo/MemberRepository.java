package com.sdatithe.repo;

import com.sdatithe.model.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPhone(String phone);
    Optional<Member> findByEmailIgnoreCase(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "church")
    List<Member> findByChurch_IdOrderByFullNameAsc(Long churchId);

    @EntityGraph(attributePaths = "church")
    @Query("select m from Member m where lower(m.fullName) like lower(concat('%', :q, '%')) or m.phone like concat('%', :q, '%') order by m.fullName")
    List<Member> search(@Param("q") String q, Pageable pageable);
}

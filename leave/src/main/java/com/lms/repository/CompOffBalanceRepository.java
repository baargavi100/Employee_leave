package com.lms.repository;

import com.lms.entity.CompOffBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CompOffBalanceRepository extends JpaRepository<CompOffBalance, Long> {
    Optional<CompOffBalance> findByEmployeeIdAndYear(Long employeeId, Integer year);
    List<CompOffBalance> findByYear(Integer year);
}

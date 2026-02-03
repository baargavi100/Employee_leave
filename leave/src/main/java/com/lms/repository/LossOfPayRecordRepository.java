package com.lms.repository;

import com.lms.entity.LossOfPayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface LossOfPayRecordRepository extends JpaRepository<LossOfPayRecord, Long> {
    Optional<LossOfPayRecord> findByEmployeeIdAndYearAndMonth(Long empId, Integer year, Integer month);

    @Query("SELECT COALESCE(SUM(l.totalLopPercentage), 0.0) FROM LossOfPayRecord l " +
            "WHERE l.employeeId = :empId AND l.year = :year")
    Double getTotalLopForYear(Long empId, Integer year);

    List<LossOfPayRecord> findByEmployeeIdAndYear(Long empId, Integer year);
}
package com.lms.repository;

import com.lms.entity.LossOfPayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LossOfPayRecordRepository extends JpaRepository<LossOfPayRecord, Long> {

    Optional<LossOfPayRecord> findByEmployeeIdAndYearAndMonth(
            Long employeeId,
            int year,
            int month
    );

    @Query("""
        SELECT SUM(l.lopPercentage)
        FROM LossOfPayRecord l
        WHERE l.employee.id = :empId
          AND l.year = :year
    """)
    Double getTotalLopForYear(Long empId, int year);
}

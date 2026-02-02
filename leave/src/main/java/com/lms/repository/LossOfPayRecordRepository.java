package com.lms.repository;

import com.lms.entity.LossOfPayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LossOfPayRecordRepository extends JpaRepository<LossOfPayRecord, Long> {
    Optional<LossOfPayRecord> findByEmployeeIdAndYearAndMonth(Long empId, Integer year, Integer month);

    @Query("SELECT SUM(l.totalLopPercentage) FROM LossOfPayRecord l " +
            "WHERE l.employee.id = :empId AND l.year = :year")
    Double getTotalLopForYear(Long empId, Integer year);
}
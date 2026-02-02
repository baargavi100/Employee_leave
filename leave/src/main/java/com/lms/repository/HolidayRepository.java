package com.lms.repository;

import com.lms.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Holiday h WHERE h.date = :date")
    boolean isHoliday(LocalDate date);

    List<Holiday> findByYear(Integer year);
}
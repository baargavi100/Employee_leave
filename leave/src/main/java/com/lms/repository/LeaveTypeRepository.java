package com.lms.repository;

import com.lms.entity.LeaveType;
import com.lms.enums.LeaveCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    Optional<LeaveType> findByCategory(LeaveCategory category);

    boolean existsByCategory(LeaveCategory category);
}

package com.lms.repository;

import com.lms.entity.LeaveAllocation;
import com.lms.enums.LeaveCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveAllocationRepository extends JpaRepository<LeaveAllocation, Long> {

    List<LeaveAllocation> findByEmployeeIdAndYear(Long employeeId, int year);

    Optional<LeaveAllocation> findByEmployeeIdAndLeaveCategoryAndYear(
            Long employeeId,
            LeaveCategory leaveCategory,
            int year
    );
}

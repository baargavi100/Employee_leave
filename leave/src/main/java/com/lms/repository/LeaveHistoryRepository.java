package com.lms.repository;

import com.lms.entity.LeaveHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveHistoryRepository extends JpaRepository<LeaveHistory, Long> {

    List<LeaveHistory> findByLeaveRequestIdOrderByChangedAtDesc(Long leaveRequestId);

    List<LeaveHistory> findByLeaveRequestEmployeeIdOrderByChangedAtDesc(Long employeeId);
}

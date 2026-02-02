package com.lms.repository;

import com.lms.entity.CompOffRequest;
import com.lms.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompOffRequestRepository extends JpaRepository<CompOffRequest, Long> {

    @Query("SELECT c FROM CompOffRequest c WHERE c.employee.manager.id = :managerId AND c.status = 'PENDING'")
    List<CompOffRequest> findPendingForManager(@Param("managerId") Long managerId);

    List<CompOffRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    @Query("SELECT c FROM CompOffRequest c WHERE c.employee.id = :empId ORDER BY c.createdAt DESC")
    List<CompOffRequest> findByEmployeeIdOrderByCreatedAtDesc(@Param("empId") Long empId);
}
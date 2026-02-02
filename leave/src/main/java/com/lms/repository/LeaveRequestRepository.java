package com.lms.repository;

import com.lms.entity.LeaveRequest;
import com.lms.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // CRITICAL: Get used days per category (APPROVED only)
    @Query("SELECT lr.leaveCategory, SUM(lr.totalDays) FROM LeaveRequest lr " +
            "WHERE lr.employee.id = :empId AND lr.status = 'APPROVED' " +
            "AND lr.year = :year GROUP BY lr.leaveCategory")
    List<Object[]> getUsedDaysByCategory(@Param("empId") Long empId, @Param("year") Integer year);

    // Count approved leaves in a specific month
    @Query("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.employee.id = :empId " +
            "AND lr.status = 'APPROVED' AND lr.year = :year AND lr.month = :month")
    Long countApprovedInMonth(@Param("empId") Long empId,
                              @Param("year") Integer year,
                              @Param("month") Integer month);

    // Count half-days for a category
    @Query("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.employee.id = :empId " +
            "AND lr.leaveCategory = :cat AND lr.status = 'APPROVED' " +
            "AND lr.totalDays = 0.5 AND lr.year = :year")
    Long countHalfDays(@Param("empId") Long empId,
                       @Param("cat") LeaveCategory cat,
                       @Param("year") Integer year);

    // Find pending requests for manager
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.manager.id = :managerId " +
            "AND lr.status = 'PENDING' ORDER BY lr.createdAt")
    List<LeaveRequest> findPendingForManager(@Param("managerId") Long managerId);

    // Employee's leave history
    List<LeaveRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    // CRITICAL: Check if already processed (duplicate prevention)
    @Query("SELECT CASE WHEN COUNT(lr) > 0 THEN true ELSE false END FROM LeaveRequest lr " +
            "WHERE lr.id = :id AND lr.status IN ('APPROVED', 'REJECTED')")
    boolean isAlreadyProcessed(@Param("id") Long id);

    // Get monthly statistics for employee
    @Query("SELECT lr.leaveCategory, COUNT(lr), SUM(lr.totalDays) FROM LeaveRequest lr " +
            "WHERE lr.employee.id = :empId AND lr.status = 'APPROVED' " +
            "AND lr.year = :year AND lr.month = :month GROUP BY lr.leaveCategory")
    List<Object[]> getMonthlyStats(@Param("empId") Long empId,
                                   @Param("year") Integer year,
                                   @Param("month") Integer month);

    // Count comp-off usage
    @Query("SELECT SUM(lr.compOffDaysUsed) FROM LeaveRequest lr " +
            "WHERE lr.employee.id = :empId AND lr.status = 'APPROVED' " +
            "AND lr.usesCompOff = true AND lr.year = :year")
    Double getTotalCompOffUsed(@Param("empId") Long empId, @Param("year") Integer year);
}
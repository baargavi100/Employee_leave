package com.lms.repository;

import com.lms.entity.LeaveRequest;
import com.lms.enums.LeaveCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // All leaves of an employee in a year
    List<LeaveRequest> findByEmployeeIdAndYear(Long employeeId, int year);

    // Used leave days per category (APPROVED)
    @Query("""
        SELECT lr.leaveCategory, SUM(lr.totalDays)
        FROM LeaveRequest lr
        WHERE lr.employee.id = :empId
          AND lr.status = 'APPROVED'
          AND lr.year = :year
        GROUP BY lr.leaveCategory
    """)
    List<Object[]> getUsedDaysByCategory(
            @Param("empId") Long empId,
            @Param("year") int year
    );

    // Monthly approved count
    @Query("""
        SELECT COUNT(lr)
        FROM LeaveRequest lr
        WHERE lr.employee.id = :empId
          AND lr.status = 'APPROVED'
          AND lr.year = :year
          AND lr.month = :month
    """)
    long countApprovedInMonth(
            @Param("empId") Long empId,
            @Param("year") int year,
            @Param("month") int month
    );

    // Half-day count
    @Query("""
        SELECT COUNT(lr)
        FROM LeaveRequest lr
        WHERE lr.employee.id = :empId
          AND lr.leaveCategory = :category
          AND lr.status = 'APPROVED'
          AND lr.totalDays = 0.5
          AND lr.year = :year
    """)
    long countHalfDays(
            @Param("empId") Long empId,
            @Param("category") LeaveCategory category,
            @Param("year") int year
    );

    // Pending approvals for manager
    @Query("""
        SELECT lr
        FROM LeaveRequest lr
        WHERE lr.employee.manager.id = :managerId
          AND lr.status = 'PENDING'
        ORDER BY lr.createdAt
    """)
    List<LeaveRequest> findPendingForManager(@Param("managerId") Long managerId);

    // History
    List<LeaveRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    // Prevent double approve/reject
    @Query("""
        SELECT CASE WHEN COUNT(lr) > 0 THEN true ELSE false END
        FROM LeaveRequest lr
        WHERE lr.id = :leaveId
          AND lr.status IN ('APPROVED','REJECTED')
    """)
    boolean isAlreadyProcessed(@Param("leaveId") Long leaveId);
}

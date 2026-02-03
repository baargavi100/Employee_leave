package com.lms.repository;

import com.lms.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    /**
     * CRITICAL: Get used days per category (APPROVED only)
     */
    @Query("SELECT lr.leaveCategory, SUM(lr.totalDays) FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :empId AND lr.status = 'APPROVED' " +
            "AND lr.year = :year GROUP BY lr.leaveCategory")
    List<Object[]> getUsedDaysByCategory(@Param("empId") Long empId, @Param("year") Integer year);

    /**
     * Count approved leaves in a specific month
     */
    @Query("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.employeeId = :empId " +
            "AND lr.status = 'APPROVED' AND lr.year = :year AND lr.month = :month")
    Long countApprovedInMonth(@Param("empId") Long empId,
                              @Param("year") Integer year,
                              @Param("month") Integer month);

    /**
     * Count half-days for a category
     */
    @Query("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.employeeId = :empId " +
            "AND lr.leaveCategory = :cat AND lr.status = 'APPROVED' " +
            "AND lr.totalDays = 0.5 AND lr.year = :year")
    Long countHalfDays(@Param("empId") Long empId,
                       @Param("cat") String cat,
                       @Param("year") Integer year);

    /**
     * Get all leaves for employee
     */
    List<LeaveRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    /**
     * Get pending leaves for manager
     */
    @Query("SELECT lr FROM LeaveRequest lr JOIN User u ON lr.employeeId = u.id " +
            "WHERE u.managerId = :managerId AND lr.status = 'PENDING' " +
            "ORDER BY lr.createdAt")
    List<LeaveRequest> findPendingForManager(@Param("managerId") Long managerId);

    /**
     * Get monthly statistics
     */
    @Query("SELECT lr.leaveCategory, COUNT(lr), SUM(lr.totalDays) FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :empId AND lr.status = 'APPROVED' " +
            "AND lr.year = :year AND lr.month = :month GROUP BY lr.leaveCategory")
    List<Object[]> getMonthlyStats(@Param("empId") Long empId,
                                   @Param("year") Integer year,
                                   @Param("month") Integer month);

    /**
     * Get total remaining days for carry forward calculation
     */
    @Query("SELECT SUM(lr.totalDays) FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :empId AND lr.status = 'APPROVED' AND lr.year = :year")
    Double getTotalUsedDays(@Param("empId") Long empId, @Param("year") Integer year);
}
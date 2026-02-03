
package com.lms.entity;

import jakarta.persistence.*;
        import lombok.*;
        import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "leave_category", nullable = false)
    private String leaveCategory;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_days")
    private Double totalDays;  // Supports 0.5 for half-days

    @Column(name = "half_day_type")
    private String halfDayType = "FULL_DAY";  // FIRST_HALF, SECOND_HALF, FULL_DAY

    @Column(nullable = false)
    private String status = "PENDING";  // PENDING, APPROVED, REJECTED

    private String reason;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer year;
    private Integer month;

    @Column(name = "uses_comp_off")
    private Boolean usesCompOff = false;

    @Column(name = "comp_off_days_used")
    private Double compOffDaysUsed = 0.0;
}

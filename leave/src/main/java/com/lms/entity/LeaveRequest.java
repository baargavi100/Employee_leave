package com.lms.entity;

import com.lms.enums.*;
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

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Enumerated(EnumType.STRING)
    private LeaveCategory leaveCategory;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalDays;

    @Enumerated(EnumType.STRING)
    private HalfDayType halfDayType = HalfDayType.FULL_DAY;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;

    private String reason;

    // WHO APPROVED / WHEN
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // For monthly tracking
    private Integer year;
    private Integer month;

    // NEW: If this leave uses comp-off
    @Column(name = "uses_comp_off")
    private Boolean usesCompOff = false;

    @Column(name = "comp_off_days_used")
    private Double compOffDaysUsed = 0.0;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        if (startDate != null) {
            year = startDate.getYear();
            month = startDate.getMonthValue();
        }
    }
}
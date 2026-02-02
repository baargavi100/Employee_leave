package com.lms.entity;

import com.lms.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comp_off_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompOffRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(name = "holiday_work_date")
    private LocalDate holidayWorkDate;

    private String reason;

    @Column(name = "days_earned")
    private Integer daysEarned = 1;  // Typically 1 day per holiday worked

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
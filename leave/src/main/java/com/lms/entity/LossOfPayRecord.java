package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loss_of_pay_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossOfPayRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    private Integer year;
    private Integer month;

    @Column(name = "monthly_violation_lop")
    private Double monthlyViolationLop = 0.0;  // From >2 monthly approvals

    @Column(name = "comp_off_negative_lop")
    private Double compOffNegativeLop = 0.0;   // From negative comp-off

    @Column(name = "total_lop_percentage")
    private Double totalLopPercentage = 0.0;   // Auto-calculated sum

    @Column(name = "monthly_violation_count")
    private Integer monthlyViolationCount = 0;

    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    void onUpdate() {
        // CRITICAL: Auto-calculate total LOP
        this.totalLopPercentage = this.monthlyViolationLop + this.compOffNegativeLop;
        this.updatedAt = LocalDateTime.now();
    }
}
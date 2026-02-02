package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loss_of_pay_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "year", "month"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossOfPayRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    private Integer year;
    private Integer month;

    @Column(name = "monthly_violation_lop")
    private Double monthlyViolationLop = 0.0;  // From >2 monthly approvals

    @Column(name = "comp_off_negative_lop")
    private Double compOffNegativeLop = 0.0;   // From negative comp-off

    @Column(name = "total_lop_percentage")
    private Double totalLopPercentage = 0.0;   // Sum of both

    @Column(name = "monthly_violation_count")
    private Integer monthlyViolationCount = 0;

    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Auto-calculate total
        totalLopPercentage = monthlyViolationLop + compOffNegativeLop;
    }
}
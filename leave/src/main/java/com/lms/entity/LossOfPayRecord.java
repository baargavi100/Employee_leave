package com.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    // Total LOP percentage accumulated
    @Column(name = "lop_percentage", nullable = false)
    private Double lopPercentage = 0.0;

    // Number of violations in the month
    @Column(name = "violation_count", nullable = false)
    private Integer violationCount = 0;

    @Column(name = "reason")
    private String reason;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

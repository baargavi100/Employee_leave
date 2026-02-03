package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comp_off_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompOffBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    private Integer year;

    private Double earned = 0.0;
    private Double used = 0.0;
    private Double balance = 0.0;  // Can be negative

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

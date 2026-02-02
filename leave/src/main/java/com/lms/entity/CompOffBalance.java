package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comp_off_balance", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompOffBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    private Integer year;

    private Double earned = 0.0;      // Total earned
    private Double used = 0.0;        // Total used
    private Double balance = 0.0;     // earned - used (can be negative)

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }
}
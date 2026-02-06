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

    // ═══════════════════════════════════════════════════════════════
    // OLD CODE:
    // private Double balance = 0.0;  // Can be negative
    //
    // NEW CODE: Separate tracking
    // ═══════════════════════════════════════════════════════════════
    private Double earned = 0.0;      // Total earned from approved comp-off requests
    private Double used = 0.0;        // Total used when taking comp-off leaves
    private Double balance = 0.0;     // earned - used (NEVER NEGATIVE)

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Calculate balance (earned - used)
     * CRITICAL: Balance should never be negative
     */
    public void calculateBalance() {
        this.balance = this.earned - this.used;
        if (this.balance < 0) {
            this.balance = 0.0;  // Never allow negative
        }
    }
}

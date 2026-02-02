package com.lms.entity;

import com.lms.enums.LeaveCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_allocations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "leave_category", "year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_category")
    private LeaveCategory leaveCategory;

    private Integer year;

    @Column(name = "allocated_days")
    private Double allocatedDays;

    @Column(name = "carried_forward_days")
    private Double carriedForwardDays = 0.0;
}
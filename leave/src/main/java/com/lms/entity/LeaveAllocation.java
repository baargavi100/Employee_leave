package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_allocations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "leave_category", nullable = false)
    private String leaveCategory;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "allocated_days", nullable = false)
    private Double allocatedDays;

    @Column(name = "carried_forward_days")
    private Double carriedForwardDays = 0.0;
}

package com.lms.entity;

import com.lms.enums.LeaveCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_allocations",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"employee_id", "leave_category", "year"}))
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
    private LeaveCategory leaveCategory;

    private int year;
    private double allocatedDays;
    private double carriedForwardDays;
}

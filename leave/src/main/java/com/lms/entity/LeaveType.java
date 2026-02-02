package com.lms.entity;

import com.lms.enums.LeaveCategory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leave_types")
@Data
@NoArgsConstructor
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private LeaveCategory category;

    @Column(nullable = false)
    private int defaultAllocation;

    private String description;
}

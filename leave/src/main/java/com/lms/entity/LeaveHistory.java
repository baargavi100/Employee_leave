package com.lms.entity;

import com.lms.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LeaveRequest leaveRequest;

    @Enumerated(EnumType.STRING)
    private LeaveStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private LeaveStatus newStatus;

    @ManyToOne
    private User changedBy;

    private LocalDateTime changedAt;
    private String notes;
}

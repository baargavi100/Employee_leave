package com.lms.entity;

import com.lms.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Enumerated(EnumType.STRING)
    private LeaveCategory leaveCategory;

    private LocalDate startDate;
    private LocalDate endDate;

    private double totalDays;

    @Enumerated(EnumType.STRING)
    private HalfDayType halfDayType;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;

    private int year;
    private int month;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        if (startDate != null) {
            year = startDate.getYear();
            month = startDate.getMonthValue();
        }
    }
}

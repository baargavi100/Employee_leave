package com.lms.dto.response;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveHistoryResponse {
    private Long leaveId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalDays;
    private String status;

    // Who applied
    private String appliedBy;
    private LocalDateTime appliedAt;

    // Who approved
    private String approvedBy;
    private LocalDateTime approvedAt;

    private String reason;

    // Comp-off fields
    private Boolean usesCompOff;
    private Double compOffDaysUsed;
}
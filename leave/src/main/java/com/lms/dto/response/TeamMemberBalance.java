package com.lms.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberBalance {
    private Long employeeId;
    private String employeeName;
    private Double totalAllocated;
    private Double totalUsed;
    private Double totalRemaining;
    private Double compOffBalance;
    private Double lopPercentage;
    private Integer totalWorkingDays;
}
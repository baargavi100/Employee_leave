package com.lms.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeBreakdown {
    private String leaveType;
    private Double allocatedDays;      // Base allocated (or earned for comp-off)
    private Double usedDays;           // Days used
    private Double remainingDays;      // Allocated - used
    private Integer halfDayCount;      // Count of 0.5 day leaves
}

package com.lms.dto.response;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeBreakdown {
    private String leaveType;
    private Double allocatedDays;
    private Double usedDays;
    private Double remainingDays;
    private Integer halfDayCount;
}
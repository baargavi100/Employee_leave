package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeBreakdown {

    private String leaveType;
    private double allocatedDays;
    private double usedDays;
    private double remainingDays;
    private long halfDayCount;
}

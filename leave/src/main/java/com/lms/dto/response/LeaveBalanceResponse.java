package com.lms.dto.response;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalanceResponse {
    private Long employeeId;
    private String employeeName;
    private Integer year;

    // Total leave statistics
    private Double totalAllocated;
    private Double totalUsed;
    private Double totalRemaining;

    // ═══════════════════════════════════════════════════════════════
    // OLD CODE:
    // private Boolean compOffNegative;  // ❌ REMOVED
    //
    // NEW CODE: Comp-off never negative
    // ═══════════════════════════════════════════════════════════════
    private Double compOffBalance;     // Always >= 0
    private Double compOffEarned;
    private Double compOffUsed;

    // Loss of Pay (only from monthly violations)
    private Double lopPercentage;

    // Carry Forward
    private Double carriedFromLastYear;
    private Double eligibleToCarry;

    // Monthly Stats
    private Integer currentMonthApproved;
    private Boolean exceededMonthlyLimit;

    // Working Days
    private Integer totalWorkingDays;

    // Breakdown per leave type
    private List<LeaveTypeBreakdown> breakdown;
}
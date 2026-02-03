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
    private Double totalAllocated;     // Base 24 + carried forward
    private Double totalUsed;          // ONLY APPROVED leaves
    private Double totalRemaining;     // allocated - used

    // Comp Off (appears in breakdown)
    private Double compOffBalance;     // Can be negative
    private Boolean compOffNegative;   // True if balance < 0
    private Double compOffEarned;      // Total earned
    private Double compOffUsed;        // Total used

    // Loss of Pay (cumulative)
    private Double lopPercentage;      // Total LOP for year

    // Carry Forward
    private Double carriedFromLastYear;  // Carried from previous year
    private Double eligibleToCarry;      // Can carry to next year (0 or 10)

    // Monthly Stats
    private Integer currentMonthApproved;  // Approved count this month
    private Boolean exceededMonthlyLimit;  // True if >2 this month

    // Working Days
    private Integer totalWorkingDays;    // Total working days

    // Breakdown per leave type (INCLUDING COMP_OFF)
    private List<LeaveTypeBreakdown> breakdown;
}
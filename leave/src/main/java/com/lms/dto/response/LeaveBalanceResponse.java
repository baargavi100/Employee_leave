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

    // Totals
    private Double totalAllocated;
    private Double totalUsed;
    private Double totalRemaining;
    private Double earnedLeaves;

    // Comp Off (ENHANCED)
    private Double compOffBalance;
    private Boolean compOffNegative;
    private Double compOffEarned;     // NEW
    private Double compOffUsed;       // NEW

    // Loss of Pay
    private Double lopPercentage;
    private Integer monthlyViolations;

    // Carry Forward
    private Double carriedFromLastYear;
    private Double eligibleToCarry;

    // Monthly Stats
    private Integer currentMonthApproved;
    private Boolean exceededMonthlyLimit;

    // Working Days
    private Integer totalWorkingDays;  // NEW

    // Breakdown
    private List<LeaveTypeBreakdown> breakdown;
}

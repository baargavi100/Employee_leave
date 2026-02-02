package com.lms.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class LeaveBalanceResponse {

    private Long employeeId;
    private String employeeName;
    private int year;

    // Totals
    private double totalAllocated;
    private double totalUsed;
    private double totalRemaining;

    // Comp Off
    private double compOffBalance;
    private boolean compOffNegative;

    // Loss Of Pay
    private double lopPercentage;

    // Carry forward
    private double eligibleToCarry;

    // Monthly stats
    private int currentMonthApproved;
    private boolean exceededMonthlyLimit;

    // Per leave type breakdown
    private List<LeaveTypeBreakdown> breakdown;
}

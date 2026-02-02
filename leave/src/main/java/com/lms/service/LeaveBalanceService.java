package com.lms.service;

import com.lms.dto.response.*;
import com.lms.entity.*;
import com.lms.enums.LeaveCategory;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final UserRepository userRepo;
    private final LeaveAllocationRepository allocationRepo;
    private final LeaveRequestRepository requestRepo;
    private final CompOffBalanceRepository compOffRepo;
    private final LossOfPayRecordRepository lopRepo;

    private static final int MAX_CARRY_FORWARD = 10;

    /**
     * ====================================================================
     * MAIN METHOD: Get complete leave balance with comp-off breakdown
     * ====================================================================
     */
    @Transactional(readOnly = true)
    public LeaveBalanceResponse getBalance(Long employeeId, Integer year) {
        log.info("[BALANCE] Calculating for employee={}, year={}", employeeId, year);

        User employee = userRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        // 1. Get allocations
        List<LeaveAllocation> allocations = allocationRepo
                .findByEmployeeIdAndYear(employeeId, year);

        // 2. Get APPROVED leaves grouped by category
        List<Object[]> usedRaw = requestRepo.getUsedDaysByCategory(employeeId, year);
        Map<LeaveCategory, Double> usedMap = usedRaw.stream()
                .collect(Collectors.toMap(
                        row -> (LeaveCategory) row[0],
                        row -> ((Number) row[1]).doubleValue()
                ));

        // 3. Build breakdown per leave type
        List<LeaveTypeBreakdown> breakdown = new ArrayList<>();
        double totalAllocated = 0;
        double totalUsed = 0;

        for (LeaveAllocation alloc : allocations) {
            double allocated = alloc.getAllocatedDays() + alloc.getCarriedForwardDays();
            double used = usedMap.getOrDefault(alloc.getLeaveCategory(), 0.0);
            Long halfDays = requestRepo.countHalfDays(employeeId, alloc.getLeaveCategory(), year);

            breakdown.add(new LeaveTypeBreakdown(
                    alloc.getLeaveCategory().name(),
                    allocated,
                    used,
                    allocated - used,
                    halfDays.intValue()
            ));

            totalAllocated += allocated;
            totalUsed += used;
        }

        // 4. Get comp-off balance (CRITICAL: Shows in breakdown)
        CompOffBalance compOff = compOffRepo.findByEmployeeIdAndYear(employeeId, year)
                .orElse(null);

        double compOffEarned = (compOff != null) ? compOff.getEarned() : 0;
        double compOffUsed = (compOff != null) ? compOff.getUsed() : 0;
        double compOffBalance = (compOff != null) ? compOff.getBalance() : 0;

        // Add comp-off to breakdown
        breakdown.add(new LeaveTypeBreakdown(
                "COMP_OFF",
                compOffEarned,
                compOffUsed,
                compOffBalance,
                0  // No half-days for comp-off
        ));

        // 5. Get LOP (cumulative)
        Double lopTotal = lopRepo.getTotalLopForYear(employeeId, year);
        double lop = (lopTotal != null) ? lopTotal : 0.0;

        // 6. Monthly stats
        int currentMonth = LocalDate.now().getMonthValue();
        Long monthApproved = requestRepo.countApprovedInMonth(employeeId, year, currentMonth);

        // 7. Carry forward calculation
        double remaining = totalAllocated - totalUsed;
        double eligibleCarry = Math.min(remaining, MAX_CARRY_FORWARD);

        // 8. Carried from last year
        double carriedForward = allocations.stream()
                .mapToDouble(LeaveAllocation::getCarriedForwardDays)
                .sum();

        // 9. Build response
        LeaveBalanceResponse response = new LeaveBalanceResponse();
        response.setEmployeeId(employeeId);
        response.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        response.setYear(year);
        response.setTotalAllocated(totalAllocated);
        response.setTotalUsed(totalUsed);
        response.setTotalRemaining(remaining);
        response.setCompOffBalance(compOffBalance);
        response.setCompOffNegative(compOffBalance < 0);
        response.setCompOffEarned(compOffEarned);
        response.setCompOffUsed(compOffUsed);
        response.setLopPercentage(lop);
        response.setEligibleToCarry(eligibleCarry);
        response.setCarriedFromLastYear(carriedForward);
        response.setCurrentMonthApproved(monthApproved.intValue());
        response.setExceededMonthlyLimit(monthApproved > 2);
        response.setBreakdown(breakdown);


        log.info("[BALANCE] Calculated: allocated={}, used={}, compOff={}, LOP={}%",
                totalAllocated, totalUsed, compOffBalance, lop);

        return response;
    }
}
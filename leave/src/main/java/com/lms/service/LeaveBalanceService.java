package com.lms.service;

import com.lms.dto.response.LeaveBalanceResponse;
import com.lms.dto.response.LeaveTypeBreakdown;
import com.lms.entity.CompOffBalance;
import com.lms.entity.LeaveAllocation;
import com.lms.entity.User;
import com.lms.enums.LeaveCategory;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final UserRepository userRepo;
    private final LeaveAllocationRepository allocationRepo;
    private final LeaveRequestRepository requestRepo;
    private final CompOffBalanceRepository compOffRepo;
    private final LossOfPayRecordRepository lopRepo;

    private static final int MAX_CARRY_FORWARD = 10;

    @Transactional(readOnly = true)
    public LeaveBalanceResponse getBalance(Long employeeId, int year) {

        User employee = userRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<LeaveAllocation> allocations =
                allocationRepo.findByEmployeeIdAndYear(employeeId, year);

        Map<LeaveCategory, Double> usedMap =
                requestRepo.getUsedDaysByCategory(employeeId, year)
                        .stream()
                        .collect(Collectors.toMap(
                                r -> (LeaveCategory) r[0],
                                r -> ((Number) r[1]).doubleValue()
                        ));

        double totalAllocated = 0;
        double totalUsed = 0;
        List<LeaveTypeBreakdown> breakdown = new ArrayList<>();

        for (LeaveAllocation alloc : allocations) {
            double allocated = alloc.getAllocatedDays() + alloc.getCarriedForwardDays();
            double used = usedMap.getOrDefault(alloc.getLeaveCategory(), 0.0);

            breakdown.add(new LeaveTypeBreakdown(
                    alloc.getLeaveCategory().name(),
                    allocated,
                    used,
                    allocated - used,
                    requestRepo.countHalfDays(
                            employeeId,
                            alloc.getLeaveCategory(),
                            year
                    )
            ));

            totalAllocated += allocated;
            totalUsed += used;
        }

        CompOffBalance compOff =
                compOffRepo.findByEmployeeIdAndYear(employeeId, year).orElse(null);

        double remaining = totalAllocated - totalUsed;

        LeaveBalanceResponse res = new LeaveBalanceResponse();
        res.setEmployeeId(employeeId);
        res.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        res.setYear(year);
        res.setTotalAllocated(totalAllocated);
        res.setTotalUsed(totalUsed);
        res.setTotalRemaining(remaining);
        res.setCompOffBalance(compOff != null ? compOff.getBalance() : 0);
        res.setCompOffNegative(compOff != null && compOff.getBalance() < 0);
        res.setLopPercentage(
                Optional.ofNullable(lopRepo.getTotalLopForYear(employeeId, year)).orElse(0.0)
        );
        res.setEligibleToCarry(Math.min(remaining, MAX_CARRY_FORWARD));
        res.setCurrentMonthApproved(
                (int) requestRepo.countApprovedInMonth(
                        employeeId,
                        year,
                        LocalDate.now().getMonthValue()
                )
        );
        res.setExceededMonthlyLimit(res.getCurrentMonthApproved() > 2);
        res.setBreakdown(breakdown);

        return res;
    }
}

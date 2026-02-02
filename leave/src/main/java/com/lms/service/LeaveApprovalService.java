package com.lms.service;

import com.lms.entity.*;
import com.lms.enums.LeaveStatus;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaveApprovalService {

    private final LeaveRequestRepository requestRepo;
    private final LeaveAllocationRepository allocationRepo;
    private final UserRepository userRepo;
    private final LeaveHistoryService historyService;
    private final LossOfPayService lopService;
    private final CompOffService compOffService;

    /**
     * ====================================================================
     * APPROVE LEAVE - Complete with all validations
     * ====================================================================
     */
    @Transactional
    public void approveLeave(Long leaveId, Long managerId) {
        log.info("[APPROVAL] Processing: leave={}, manager={}", leaveId, managerId);

        // 1. CRITICAL: Check duplicate processing
        if (requestRepo.isAlreadyProcessed(leaveId)) {
            throw new RuntimeException("Leave already processed - cannot approve again");
        }

        LeaveRequest leave = requestRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        User manager = userRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        // 2. CRITICAL: Validate carry forward limit (≤10 days)
        if (leave.getEmployee() != null) {
            validateCarryForwardLimit(leave.getEmployee().getId(), leave.getYear());
        }

        LeaveStatus oldStatus = leave.getStatus();

        // 3. Update leave request
        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedBy(manager);
        leave.setApprovedAt(LocalDateTime.now());

        requestRepo.save(leave);

        // 4. If uses comp-off, deduct from balance
        if (Boolean.TRUE.equals(leave.getUsesCompOff()) && leave.getCompOffDaysUsed() > 0) {
            compOffService.spendCompOff(
                    leave.getEmployee().getId(),
                    leave.getYear(),
                    leave.getCompOffDaysUsed()
            );
        }

        // 5. Record in history
        historyService.recordStatusChange(leave, oldStatus, LeaveStatus.APPROVED,
                manager, "Approved by manager");

        // 6. CRITICAL: Check monthly limit (>2 triggers LOP)
        Long monthCount = requestRepo.countApprovedInMonth(
                leave.getEmployee().getId(), leave.getYear(), leave.getMonth());

        if (monthCount > 2) {
            log.warn("[APPROVAL] Monthly limit exceeded: emp={}, month={}/{}, count={}",
                    leave.getEmployee().getId(), leave.getMonth(), leave.getYear(), monthCount);

            lopService.applyMonthlyLimitViolation(
                    leave.getEmployee().getId(), leave.getYear(), leave.getMonth());
        }



        log.info("[APPROVAL] Approved: leave={}, employee={}, approved_at={}",
                leaveId, leave.getEmployee().getId(), leave.getApprovedAt());
    }

    /**
     * ====================================================================
     * REJECT LEAVE
     * ====================================================================
     */
    @Transactional
    public void rejectLeave(Long leaveId, Long managerId, String reason) {
        if (requestRepo.isAlreadyProcessed(leaveId)) {
            throw new RuntimeException("Leave already processed");
        }

        LeaveRequest leave = requestRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        User manager = userRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        LeaveStatus oldStatus = leave.getStatus();

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedBy(manager);
        leave.setApprovedAt(LocalDateTime.now());

        requestRepo.save(leave);

        historyService.recordStatusChange(leave, oldStatus, LeaveStatus.REJECTED,
                manager, reason);

        log.info("[APPROVAL] Rejected: leave={}, manager={}", leaveId, managerId);
    }

    /**
     * ====================================================================
     * CRITICAL: Validate carry forward limit (max 10 days per year)
     * ====================================================================
     */
    private void validateCarryForwardLimit(Long employeeId, Integer year) {
        double totalCarried = allocationRepo.findByEmployeeIdAndYear(employeeId, year)
                .stream()
                .mapToDouble(LeaveAllocation::getCarriedForwardDays)
                .sum();

        if (totalCarried > 10) {
            log.warn("[CARRY-FORWARD] Employee {} exceeded limit: {} days carried",
                    employeeId, totalCarried);
            throw new RuntimeException(
                    "Cannot approve: Total carried forward (" + totalCarried +
                            " days) exceeds maximum of 10 days per year");
        }
    }

    /**
     * Get pending count for manager
     */
    public int getPendingCount(Long managerId) {
        return requestRepo.findPendingForManager(managerId).size();
    }

    /**
     * Get pending leaves for manager
     */
    public java.util.List<LeaveRequest> getPendingLeaves(Long managerId) {
        return requestRepo.findPendingForManager(managerId);
    }
}
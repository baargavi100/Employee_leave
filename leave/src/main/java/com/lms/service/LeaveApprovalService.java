package com.lms.service;

import com.lms.entity.LeaveRequest;
import com.lms.entity.User;
import com.lms.enums.LeaveStatus;
import com.lms.repository.LeaveRequestRepository;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LeaveApprovalService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveHistoryService leaveHistoryService;
    private final LossOfPayService lossOfPayService;

    /**
     * APPROVE LEAVE
     */
    @Transactional
    public void approveLeave(Long leaveId, Long managerId) {

        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedBy(manager);
        leave.setApprovedAt(LocalDateTime.now());

        leaveRequestRepository.save(leave);

        leaveHistoryService.recordStatusChange(
                leave,
                LeaveStatus.PENDING,
                LeaveStatus.APPROVED,
                manager,
                "Approved"
        );

        long approvedCount =
                leaveRequestRepository.countApprovedInMonth(
                        leave.getEmployee().getId(),
                        leave.getYear(),
                        leave.getMonth()
                );

        if (approvedCount > 2) {
            lossOfPayService.incrementLopForMonthlyViolation(
                    leave.getEmployee().getId(),
                    leave.getYear(),
                    leave.getMonth()
            );
        }
    }

    /**
     * REJECT LEAVE
     */
    @Transactional
    public void rejectLeave(Long leaveId, Long managerId, String reason) {

        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedBy(manager);
        leave.setApprovedAt(LocalDateTime.now());

        leaveRequestRepository.save(leave);

        leaveHistoryService.recordStatusChange(
                leave,
                LeaveStatus.PENDING,
                LeaveStatus.REJECTED,
                manager,
                reason
        );
    }

    /**
     * 🔥 THIS METHOD WAS MISSING (CAUSE OF YOUR ERROR)
     * GET PENDING APPROVAL COUNT FOR MANAGER
     */
    public int getPendingCount(Long managerId) {
        return leaveRequestRepository
                .findPendingForManager(managerId)
                .size();
    }
}

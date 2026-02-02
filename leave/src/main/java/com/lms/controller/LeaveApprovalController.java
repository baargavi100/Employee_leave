package com.lms.controller;

import com.lms.dto.request.*;
import com.lms.dto.response.*;
import com.lms.entity.LeaveRequest;
import com.lms.service.LeaveApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveApprovalController {

    private final LeaveApprovalService approvalService;

    /**
     * POST /api/approvals/approve/{leaveId}
     * Approve a leave request
     */
    @PostMapping("/approve/{leaveId}")
    public ResponseEntity<String> approveLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveApprovalRequest request) {

        approvalService.approveLeave(leaveId, request.getManagerId());
        return ResponseEntity.ok("Leave approved successfully");
    }

    /**
     * POST /api/approvals/reject/{leaveId}
     * Reject a leave request
     */
    @PostMapping("/reject/{leaveId}")
    public ResponseEntity<String> rejectLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveRejectionRequest request) {

        approvalService.rejectLeave(leaveId, request.getManagerId(), request.getReason());
        return ResponseEntity.ok("Leave rejected successfully");
    }

    /**
     * GET /api/approvals/pending/{managerId}
     * Get count of pending approvals for manager dashboard
     */
    @GetMapping("/pending-count/{managerId}")
    public ResponseEntity<Integer> getPendingCount(@PathVariable Long managerId) {
        int count = approvalService.getPendingCount(managerId);
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/approvals/pending-leaves/{managerId}
     * Get all pending leaves for manager review
     */
    @GetMapping("/pending-leaves/{managerId}")
    public ResponseEntity<List<LeaveHistoryResponse>> getPendingLeaves(
            @PathVariable Long managerId) {

        List<LeaveRequest> leaves = approvalService.getPendingLeaves(managerId);

        List<LeaveHistoryResponse> response = leaves.stream().map(leave -> {
            LeaveHistoryResponse dto = new LeaveHistoryResponse();
            dto.setLeaveId(leave.getId());
            dto.setLeaveType(leave.getLeaveCategory().name());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setTotalDays(leave.getTotalDays());
            dto.setStatus(leave.getStatus().name());
            dto.setAppliedBy(leave.getEmployee().getFirstName() + " " +
                    leave.getEmployee().getLastName());
            dto.setAppliedAt(leave.getCreatedAt());
            dto.setReason(leave.getReason());
            dto.setUsesCompOff(leave.getUsesCompOff());
            dto.setCompOffDaysUsed(leave.getCompOffDaysUsed());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
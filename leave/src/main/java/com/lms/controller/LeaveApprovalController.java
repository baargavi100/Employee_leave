package com.lms.controller;

import com.lms.dto.request.LeaveApprovalRequest;
import com.lms.dto.request.LeaveRejectionRequest;
import com.lms.dto.response.PendingApprovalsResponse;
import com.lms.service.LeaveApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveApprovalController {

    private final LeaveApprovalService approvalService;

    /**
     * APPROVE LEAVE
     * POST /api/approvals/approve/{leaveId}
     */
    @PostMapping("/approve/{leaveId}")
    public ResponseEntity<String> approveLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveApprovalRequest request
    ) {
        approvalService.approveLeave(
                leaveId,
                request.getManagerId()
        );

        return ResponseEntity.ok("Leave approved successfully");
    }

    /**
     * REJECT LEAVE
     * POST /api/approvals/reject/{leaveId}
     */
    @PostMapping("/reject/{leaveId}")
    public ResponseEntity<String> rejectLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveRejectionRequest request
    ) {
        approvalService.rejectLeave(
                leaveId,
                request.getManagerId(),
                request.getReason()
        );

        return ResponseEntity.ok("Leave rejected successfully");
    }

    /**
     * GET PENDING APPROVALS COUNT
     * GET /api/approvals/pending/{managerId}
     */
    @GetMapping("/pending/{managerId}")
    public ResponseEntity<PendingApprovalsResponse> getPending(
            @PathVariable Long managerId
    ) {
        int count = approvalService.getPendingCount(managerId);

        PendingApprovalsResponse response = new PendingApprovalsResponse();
        response.setPendingCount(count);
        response.setThisMonth(count);
        response.setThisWeek(count);

        return ResponseEntity.ok(response);
    }
}

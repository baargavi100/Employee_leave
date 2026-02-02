package com.lms.controller;

import com.lms.dto.request.*;
import com.lms.dto.response.CompOffRequestResponse;
import com.lms.entity.CompOffRequest;
import com.lms.service.CompOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/compoff")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CompOffController {

    private final CompOffService compOffService;

    /**
     * POST /api/compoff/earn
     * Submit request to earn comp-off (worked on holiday)
     */
    @PostMapping("/earn")
    public ResponseEntity<String> earnCompOff(@RequestBody CompOffEarnRequest request) {
        compOffService.earnCompOff(
                request.getEmployeeId(),
                request.getHolidayWorkDate(),
                request.getReason()
        );
        return ResponseEntity.ok("Comp-off request submitted successfully");
    }

    /**
     * POST /api/compoff/approve/{requestId}
     * Manager approves comp-off request
     */
    @PostMapping("/approve/{requestId}")
    public ResponseEntity<String> approveCompOff(
            @PathVariable Long requestId,
            @RequestBody LeaveApprovalRequest request) {

        compOffService.approveCompOffRequest(requestId, request.getManagerId());
        return ResponseEntity.ok("Comp-off request approved");
    }

    /**
     * GET /api/compoff/pending/{managerId}
     * Get pending comp-off requests for manager
     */
    @GetMapping("/pending/{managerId}")
    public ResponseEntity<List<CompOffRequestResponse>> getPendingCompOffs(
            @PathVariable Long managerId) {

        List<CompOffRequest> requests = compOffService.getPendingForManager(managerId);

        List<CompOffRequestResponse> response = requests.stream().map(req -> {
            CompOffRequestResponse dto = new CompOffRequestResponse();
            dto.setId(req.getId());
            dto.setEmployeeId(req.getEmployee().getId());
            dto.setEmployeeName(req.getEmployee().getFirstName() + " " +
                    req.getEmployee().getLastName());
            dto.setHolidayWorkDate(req.getHolidayWorkDate());
            dto.setReason(req.getReason());
            dto.setDaysEarned(req.getDaysEarned());
            dto.setStatus(req.getStatus().name());
            dto.setCreatedAt(req.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
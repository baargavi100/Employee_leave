package com.lms.controller;

import com.lms.dto.response.LeaveHistoryResponse;
import com.lms.service.LeaveHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveHistoryController {

    private final LeaveHistoryService historyService;

    /**
     * GET /api/history/{employeeId}
     * Get complete leave history for employee
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<LeaveHistoryResponse>> getHistory(
            @PathVariable Long employeeId) {

        List<LeaveHistoryResponse> history = historyService.getEmployeeHistory(employeeId);
        return ResponseEntity.ok(history);
    }
}
package com.lms.controller;

import com.lms.entity.LeaveType;
import com.lms.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    /**
     * GET /api/leave-types
     */
    @GetMapping
    public ResponseEntity<List<LeaveType>> getAll() {
        return ResponseEntity.ok(leaveTypeService.getAllLeaveTypes());
    }

    /**
     * POST /api/leave-types
     */
    @PostMapping
    public ResponseEntity<LeaveType> create(@RequestBody LeaveType leaveType) {
        return ResponseEntity.ok(
                leaveTypeService.createLeaveType(leaveType)
        );
    }
}

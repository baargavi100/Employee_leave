//package com.lms.controller;
//
//import com.lms.dto.request.LeaveApplyRequest;
//import com.lms.entity.LeaveRequest;
//import com.lms.service.LeaveApplyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/leaves")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173")
//public class LeaveApplyController {
//
//    private final LeaveApplyService leaveApplyService;
//
//    /**
//     * POST /api/leaves/apply
//     */
//    @PostMapping("/apply")
//    public ResponseEntity<LeaveRequest> applyLeave(
//            @RequestBody LeaveApplyRequest request
//    ) {
//        LeaveRequest leave = leaveApplyService.applyLeave(request);
//        return ResponseEntity.ok(leave);
//    }
//}

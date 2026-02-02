package com.lms.controller;

import com.lms.service.CompOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compoff")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CompOffController {

    private final CompOffService compOffService;

    /**
     * POST /api/compoff/add?employeeId=2&days=1
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCompOff(
            @RequestParam Long employeeId,
            @RequestParam double days
    ) {
        compOffService.adjustCompOffBalance(employeeId, days);
        return ResponseEntity.ok("Comp-off added successfully");
    }

    /**
     * POST /api/compoff/deduct?employeeId=2&days=1
     */
    @PostMapping("/deduct")
    public ResponseEntity<String> deductCompOff(
            @RequestParam Long employeeId,
            @RequestParam double days
    ) {
        compOffService.adjustCompOffBalance(employeeId, -days);
        return ResponseEntity.ok("Comp-off deducted successfully");
    }
}

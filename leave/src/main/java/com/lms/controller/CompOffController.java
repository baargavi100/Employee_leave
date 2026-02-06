package com.lms.controller;

import com.lms.service.CompOffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compoff")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class CompOffController {

    private final CompOffService compOffService;

    /**
     * ================================================================
     * POST /api/compoff/add
     * Add comp-off days when your friend approves comp-off request
     * ================================================================
     *
     * Request Body:
     * {
     *   "employeeId": 2,
     *   "year": 2025,
     *   "days": 2.0
     * }
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCompOffDays(
            @RequestParam Long employeeId,
            @RequestParam Integer year,
            @RequestParam Double days) {

        log.info("[API] Adding comp-off: employee={}, year={}, days={}",
                employeeId, year, days);

        try {
            compOffService.addCompOffDays(employeeId, year, days);
            return ResponseEntity.ok(
                    String.format("Added %.1f comp-off days for employee %d", days, employeeId));
        } catch (Exception e) {
            log.error("[API] Error adding comp-off: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * ================================================================
     * POST /api/compoff/use
     * Deduct comp-off days when employee uses comp-off leave
     * CRITICAL: Validates sufficient balance before deducting
     * ================================================================
     *
     * Request Body:
     * {
     *   "employeeId": 2,
     *   "year": 2025,
     *   "days": 1.0
     * }
     */
    @PostMapping("/use")
    public ResponseEntity<String> useCompOffDays(
            @RequestParam Long employeeId,
            @RequestParam Integer year,
            @RequestParam Double days) {

        log.info("[API] Using comp-off: employee={}, year={}, days={}",
                employeeId, year, days);

        try {
            compOffService.useCompOffDays(employeeId, year, days);
            return ResponseEntity.ok(
                    String.format("Used %.1f comp-off days for employee %d", days, employeeId));
        } catch (IllegalStateException e) {
            log.warn("[API] Insufficient comp-off balance: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Insufficient balance: " + e.getMessage());
        } catch (Exception e) {
            log.error("[API] Error using comp-off: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * ================================================================
     * GET /api/compoff/balance/{employeeId}?year=2025
     * Get current comp-off balance
     * ================================================================
     */
    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<Double> getCompOffBalance(
            @PathVariable Long employeeId,
            @RequestParam Integer year) {

        double balance = compOffService.getCurrentBalance(employeeId, year);
        return ResponseEntity.ok(balance);
    }
}

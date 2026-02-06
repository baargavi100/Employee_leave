package com.lms.service;

import com.lms.entity.CompOffBalance;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompOffService {

    private final CompOffBalanceRepository compOffRepo;
    private final UserRepository userRepo;

    /**
     * ====================================================================
     * ADD COMP-OFF DAYS (When your friend approves comp-off request)
     * ====================================================================
     */
    @Transactional
    public void addCompOffDays(Long employeeId, Integer year, Double daysToAdd) {
        log.info("[COMP-OFF] Adding {} days for employee={}, year={}", daysToAdd, employeeId, year);

        if (daysToAdd <= 0) {
            throw new IllegalArgumentException("Days to add must be positive");
        }

        CompOffBalance balance = getOrCreateBalance(employeeId, year);

        // ═══════════════════════════════════════════════════════════════
        // OLD CODE:
        // balance.setBalance(balance.getBalance() + daysToAdd);
        //
        // NEW CODE: Add to earned
        // ═══════════════════════════════════════════════════════════════
        balance.setEarned(balance.getEarned() + daysToAdd);
        balance.calculateBalance();  // earned - used
        balance.setUpdatedAt(LocalDateTime.now());

        compOffRepo.save(balance);

        log.info("[COMP-OFF] Added: employee={}, +{} days, new balance={}",
                employeeId, daysToAdd, balance.getBalance());
    }

    /**
     * ====================================================================
     * USE COMP-OFF DAYS (When employee takes comp-off leave)
     * CRITICAL: Cannot use more than available
     * ====================================================================
     */
    @Transactional
    public void useCompOffDays(Long employeeId, Integer year, Double daysToUse) {
        log.info("[COMP-OFF] Using {} days for employee={}, year={}", daysToUse, employeeId, year);

        if (daysToUse <= 0) {
            throw new IllegalArgumentException("Days to use must be positive");
        }

        CompOffBalance balance = getOrCreateBalance(employeeId, year);

        // ═══════════════════════════════════════════════════════════════
        // OLD CODE:
        // balance.setBalance(balance.getBalance() - daysToUse);
        // if (balance.getBalance() < 0) {
        //     lopService.applyCompOffNegativePenalty(...);  // ❌ REMOVED
        // }
        //
        // NEW CODE: Validate before deducting
        // ═══════════════════════════════════════════════════════════════

        // CRITICAL: Check if enough balance available
        double currentBalance = balance.getEarned() - balance.getUsed();

        if (daysToUse > currentBalance) {
            throw new IllegalStateException(
                    String.format("Cannot use %.1f comp-off days. Available: %.1f",
                            daysToUse, currentBalance));
        }

        balance.setUsed(balance.getUsed() + daysToUse);
        balance.calculateBalance();  // earned - used (never negative)
        balance.setUpdatedAt(LocalDateTime.now());

        compOffRepo.save(balance);

        log.info("[COMP-OFF] Used: employee={}, -{} days, remaining={}",
                employeeId, daysToUse, balance.getBalance());
    }

    /**
     * Get current comp-off balance
     */
    public double getCurrentBalance(Long employeeId, Integer year) {
        CompOffBalance balance = compOffRepo.findByEmployeeIdAndYear(employeeId, year)
                .orElse(null);

        if (balance == null) {
            return 0.0;
        }

        return balance.getEarned() - balance.getUsed();
    }

    private CompOffBalance getOrCreateBalance(Long employeeId, Integer year) {
        return compOffRepo.findByEmployeeIdAndYear(employeeId, year)
                .orElseGet(() -> {
                    CompOffBalance newBalance = new CompOffBalance();
                    newBalance.setEmployeeId(employeeId);
                    newBalance.setYear(year);
                    newBalance.setEarned(0.0);
                    newBalance.setUsed(0.0);
                    newBalance.setBalance(0.0);
                    return newBalance;
                });
    }
}
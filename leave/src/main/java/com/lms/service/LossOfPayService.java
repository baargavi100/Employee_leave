package com.lms.service;

import com.lms.entity.LossOfPayRecord;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class LossOfPayService {

    private final LossOfPayRecordRepository lopRepo;
    private final UserRepository userRepo;

    /**
     * ====================================================================
     * ONLY LOP TRIGGER: Monthly limit violation (>2 approved)
     * ====================================================================
     */
    @Transactional
    public void applyMonthlyLimitViolation(Long empId, Integer year, Integer month) {
        LossOfPayRecord lop = getOrCreate(empId, year, month);

        // ═══════════════════════════════════════════════════════════════
        // OLD CODE:
        // lop.setMonthlyViolationLop(lop.getMonthlyViolationLop() + 1.0);
        // lop.setTotalLopPercentage(lop.getMonthlyViolationLop() + lop.getCompOffNegativeLop());
        //
        // NEW CODE: Only monthly violation
        // ═══════════════════════════════════════════════════════════════
        lop.setLopPercentage(lop.getLopPercentage() + 1.0);
        lop.setViolationCount(lop.getViolationCount() + 1);
        lop.setReason("Monthly limit exceeded (>2 approved leaves)");
        lop.setUpdatedAt(LocalDateTime.now());

        lopRepo.save(lop);

        log.warn("[LOP] Monthly violation: emp={}, month={}/{}, +1% → total={}%",
                empId, month, year, lop.getLopPercentage());
    }

    // ═══════════════════════════════════════════════════════════════
    // OLD CODE: (REMOVED - No comp-off LOP)
    // public void applyCompOffNegativePenalty(...) { ... }
    // ═══════════════════════════════════════════════════════════════

    private LossOfPayRecord getOrCreate(Long empId, Integer year, Integer month) {
        return lopRepo.findByEmployeeIdAndYearAndMonth(empId, year, month)
                .orElseGet(() -> {
                    LossOfPayRecord lop = new LossOfPayRecord();
                    lop.setEmployeeId(empId);
                    lop.setYear(year);
                    lop.setMonth(month);
                    lop.setLopPercentage(0.0);
                    lop.setViolationCount(0);
                    return lop;
                });
    }
}
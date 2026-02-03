package com.lms.service;

import com.lms.entity.LossOfPayRecord;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LossOfPayService {

    private final LossOfPayRecordRepository lopRepo;
    private final UserRepository userRepo;

    /**
     * ====================================================================
     * AUTO-UPDATE: Monthly limit violation → +1% LOP
     * Called when >2 leaves approved in a month
     * ====================================================================
     */
    @Transactional
    public void applyMonthlyLimitViolation(Long empId, Integer year, Integer month) {
        LossOfPayRecord lop = getOrCreate(empId, year, month);

        lop.setMonthlyViolationLop(lop.getMonthlyViolationLop() + 1.0);
        lop.setMonthlyViolationCount(lop.getMonthlyViolationCount() + 1);
        lop.setReason("Monthly limit exceeded (>2 approved leaves)");

        lopRepo.save(lop);  // @PreUpdate auto-calculates total

        log.warn("[LOP] Monthly violation: emp={}, month={}/{}, +1% → total={}%",
                empId, month, year, lop.getTotalLopPercentage());
    }

    /**
     * ====================================================================
     * AUTO-UPDATE: Comp-off negative → +1% LOP
     * Called when comp-off balance goes negative
     * ====================================================================
     */
    @Transactional
    public void applyCompOffNegativePenalty(Long empId, Integer year, Integer month) {
        LossOfPayRecord lop = getOrCreate(empId, year, month);

        lop.setCompOffNegativeLop(lop.getCompOffNegativeLop() + 1.0);
        lop.setReason("Comp-off balance went negative");

        lopRepo.save(lop);  // @PreUpdate auto-calculates total

        log.warn("[LOP] Comp-off negative: emp={}, month={}/{}, +1% → total={}%",
                empId, month, year, lop.getTotalLopPercentage());
    }

    private LossOfPayRecord getOrCreate(Long empId, Integer year, Integer month) {
        return lopRepo.findByEmployeeIdAndYearAndMonth(empId, year, month)
                .orElseGet(() -> {
                    LossOfPayRecord lop = new LossOfPayRecord();
                    lop.setEmployeeId(empId);
                    lop.setYear(year);
                    lop.setMonth(month);
                    return lop;
                });
    }
}

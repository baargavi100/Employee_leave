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
     * Monthly limit violation: +1% LOP per violation
     * ====================================================================
     */
    @Transactional
    public void applyMonthlyLimitViolation(Long empId, Integer year, Integer month) {
        LossOfPayRecord lop = getOrCreate(empId, year, month);

        lop.setMonthlyViolationLop(lop.getMonthlyViolationLop() + 1.0);
        lop.setMonthlyViolationCount(lop.getMonthlyViolationCount() + 1);
        lop.setTotalLopPercentage(lop.getMonthlyViolationLop() + lop.getCompOffNegativeLop());
        lop.setReason("Monthly limit exceeded (>2 approved leaves)");

        lopRepo.save(lop);

        log.warn("[LOP] Monthly violation: emp={}, month={}/{}, +1% → total={}%",
                empId, month, year, lop.getTotalLopPercentage());
    }

    /**
     * ====================================================================
     * Comp-off negative: +1% LOP
     * ====================================================================
     */
    @Transactional
    public void applyCompOffNegativePenalty(Long empId, Integer year, Integer month) {
        LossOfPayRecord lop = getOrCreate(empId, year, month);

        lop.setCompOffNegativeLop(lop.getCompOffNegativeLop() + 1.0);
        lop.setTotalLopPercentage(lop.getMonthlyViolationLop() + lop.getCompOffNegativeLop());
        lop.setReason("Comp-off balance went negative");

        lopRepo.save(lop);

        log.warn("[LOP] Comp-off negative: emp={}, month={}/{}, +1% → total={}%",
                empId, month, year, lop.getTotalLopPercentage());
    }

    /**
     * Get or create LOP record
     */
    private LossOfPayRecord getOrCreate(Long empId, Integer year, Integer month) {
        return lopRepo.findByEmployeeIdAndYearAndMonth(empId, year, month)
                .orElseGet(() -> {
                    LossOfPayRecord lop = new LossOfPayRecord();
                    lop.setEmployee(userRepo.findById(empId).orElseThrow());
                    lop.setYear(year);
                    lop.setMonth(month);
                    lop.setMonthlyViolationLop(0.0);
                    lop.setCompOffNegativeLop(0.0);
                    lop.setTotalLopPercentage(0.0);
                    lop.setMonthlyViolationCount(0);
                    return lop;
                });
    }
}
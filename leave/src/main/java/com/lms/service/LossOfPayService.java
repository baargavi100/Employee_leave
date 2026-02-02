package com.lms.service;

import com.lms.entity.CompOffBalance;
import com.lms.entity.LossOfPayRecord;
import com.lms.repository.CompOffBalanceRepository;
import com.lms.repository.LossOfPayRecordRepository;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LossOfPayService {

    private final LossOfPayRecordRepository lopRepo;
    private final UserRepository userRepo;
    private final CompOffBalanceRepository compOffRepo;

    @Transactional
    public void incrementLopForMonthlyViolation(Long empId, int year, int month) {

        LossOfPayRecord lop = lopRepo
                .findByEmployeeIdAndYearAndMonth(empId, year, month)
                .orElseGet(() -> {
                    LossOfPayRecord r = new LossOfPayRecord();
                    r.setEmployee(userRepo.findById(empId).orElseThrow());
                    r.setYear(year);
                    r.setMonth(month);
                    r.setLopPercentage(0);
                    r.setMonthlyViolationCount(0);
                    return r;
                });

        lop.setLopPercentage(lop.getLopPercentage() + 1);
        lopRepo.save(lop);
    }

    @Transactional
    public void checkCompOffAndApplyLop(Long empId, int year) {

        CompOffBalance cb =
                compOffRepo.findByEmployeeIdAndYear(empId, year).orElse(null);

        if (cb != null && cb.getBalance() < 0) {
            incrementLopForMonthlyViolation(
                    empId,
                    year,
                    LocalDate.now().getMonthValue()
            );
        }
    }
}

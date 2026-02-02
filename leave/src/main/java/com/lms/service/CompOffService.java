package com.lms.service;

import com.lms.entity.CompOffBalance;
import com.lms.repository.CompOffBalanceRepository;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CompOffService {

    private final CompOffBalanceRepository compOffRepo;
    private final UserRepository userRepo;
    private final LossOfPayService lopService;

    /**
     * Adjust comp-off balance (positive or negative)
     */
    @Transactional
    public void adjustCompOffBalance(Long empId, double adjustment) {

        int year = LocalDate.now().getYear();

        // Get or create comp-off balance
        CompOffBalance compOff =
                compOffRepo.findByEmployeeIdAndYear(empId, year)
                        .orElseGet(() -> {
                            CompOffBalance cb = new CompOffBalance();
                            cb.setEmployee(userRepo.findById(empId).orElseThrow());
                            cb.setYear(year);
                            cb.setBalance(0);
                            return cb;
                        });

        double oldBalance = compOff.getBalance();
        compOff.setBalance(oldBalance + adjustment);

        compOffRepo.save(compOff);

        // If balance goes from >=0 to negative → apply LOP
        if (oldBalance >= 0 && compOff.getBalance() < 0) {
            lopService.checkCompOffAndApplyLop(empId, year);
        }
    }
}

package com.lms.service;
import java.util.List;
import com.lms.entity.*;
import com.lms.enums.LeaveStatus;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompOffService {

    private final CompOffRequestRepository compOffRequestRepo;
    private final CompOffBalanceRepository compOffBalanceRepo;
    private final HolidayRepository holidayRepo;
    private final UserRepository userRepo;
    private final LossOfPayService lopService;
    private final LeaveHistoryService historyService;

    /**
     * ====================================================================
     * STEP 1: EARNING COMP-OFF (Submit request for working on holiday)
     * ====================================================================
     */
    @Transactional
    public CompOffRequest earnCompOff(Long employeeId, LocalDate holidayWorkDate, String reason) {
        log.info("[COMP-OFF] Employee {} requesting comp-off for {}", employeeId, holidayWorkDate);

        User employee = userRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // CRITICAL: Validate it was actually a holiday
        if (!isHolidayOrWeekend(holidayWorkDate)) {
            throw new IllegalArgumentException(
                    "Date " + holidayWorkDate + " was not a holiday or weekend. Comp-off only for holidays worked.");
        }

        // Create PENDING request
        CompOffRequest request = new CompOffRequest();
        request.setEmployee(employee);
        request.setHolidayWorkDate(holidayWorkDate);
        request.setReason(reason);
        request.setDaysEarned(1);  // 1 day per holiday
        request.setStatus(LeaveStatus.PENDING);

        CompOffRequest saved = compOffRequestRepo.save(request);

        log.info("[COMP-OFF] Request created: id={}, status=PENDING", saved.getId());
        return saved;
    }

    /**
     * ====================================================================
     * APPROVE COMP-OFF REQUEST (Manager approves, credits balance)
     * ====================================================================
     */
    @Transactional
    public void approveCompOffRequest(Long requestId, Long managerId) {
        CompOffRequest request = compOffRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Comp-off request not found"));

        User manager = userRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        // Update request
        request.setStatus(LeaveStatus.APPROVED);
        request.setApprovedBy(manager);
        request.setApprovedAt(LocalDateTime.now());
        compOffRequestRepo.save(request);

        // CREDIT comp-off balance
        addToCompOffBalance(request.getEmployee().getId(),
                request.getHolidayWorkDate().getYear(),
                request.getDaysEarned());

        log.info("[COMP-OFF] Approved: request={}, employee={}, credited={} days",
                requestId, request.getEmployee().getId(), request.getDaysEarned());
    }

    /**
     * ====================================================================
     * STEP 2: SPENDING COMP-OFF (Deduct when leave is approved)
     * Called by LeaveApprovalService when approving leave with usesCompOff=true
     * ====================================================================
     */
    @Transactional
    public void spendCompOff(Long employeeId, Integer year, Double daysToSpend) {
        CompOffBalance balance = getOrCreateBalance(employeeId, year);

        double oldBalance = balance.getBalance();
        balance.setUsed(balance.getUsed() + daysToSpend);
        balance.setBalance(balance.getEarned() - balance.getUsed());

        compOffBalanceRepo.save(balance);

        log.info("[COMP-OFF] Spent: employee={}, amount={}, balance: {} → {}",
                employeeId, daysToSpend, oldBalance, balance.getBalance());

        // CRITICAL: Check if went negative → trigger LOP
        if (balance.getBalance() < 0 && oldBalance >= 0) {
            int month = LocalDate.now().getMonthValue();
            lopService.applyCompOffNegativePenalty(employeeId, year, month);
        }
    }

    /**
     * Add to comp-off balance (when request approved)
     */
    private void addToCompOffBalance(Long employeeId, Integer year, Integer days) {
        CompOffBalance balance = getOrCreateBalance(employeeId, year);

        balance.setEarned(balance.getEarned() + days);
        balance.setBalance(balance.getEarned() - balance.getUsed());

        compOffBalanceRepo.save(balance);
    }

    /**
     * Get or create comp-off balance
     */
    private CompOffBalance getOrCreateBalance(Long employeeId, Integer year) {
        return compOffBalanceRepo.findByEmployeeIdAndYear(employeeId, year)
                .orElseGet(() -> {
                    CompOffBalance newBalance = new CompOffBalance();
                    newBalance.setEmployee(userRepo.findById(employeeId).orElseThrow());
                    newBalance.setYear(year);
                    newBalance.setEarned(0.0);
                    newBalance.setUsed(0.0);
                    newBalance.setBalance(0.0);
                    return newBalance;
                });
    }

    /**
     * Validate if date is holiday or weekend
     */
    private boolean isHolidayOrWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        boolean isHoliday = holidayRepo.isHoliday(date);

        log.debug("[COMP-OFF] Date {} - weekend={}, holiday={}", date, isWeekend, isHoliday);
        return isWeekend || isHoliday;
    }

    /**
     * Get pending comp-off requests for manager
     */
    public List<CompOffRequest> getPendingForManager(Long managerId) {
        return compOffRequestRepo.findPendingForManager(managerId);
    }
}

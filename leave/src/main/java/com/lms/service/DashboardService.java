package com.lms.service;

import com.lms.dto.response.*;
import com.lms.entity.User;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

    private final LeaveRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final LeaveBalanceService balanceService;

    /**
     * Employee Dashboard - Monthly statistics
     */
    @Transactional(readOnly = true)
    public MonthlyStatsResponse getMonthlyStats(Long employeeId, Integer year, Integer month) {
        log.info("[DASHBOARD] Monthly stats: employee={}, month={}/{}", employeeId, month, year);

        List<Object[]> stats = requestRepo.getMonthlyStats(employeeId, year, month);

        List<MonthlyStatsResponse.LeaveTypeStat> statsList = new ArrayList<>();
        double totalDays = 0;

        for (Object[] row : stats) {
            String leaveType = (String) row[0];
            long count = ((Number) row[1]).longValue();
            double days = ((Number) row[2]).doubleValue();

            MonthlyStatsResponse.LeaveTypeStat stat = new MonthlyStatsResponse.LeaveTypeStat();
            stat.setLeaveType(leaveType);
            stat.setCount((int) count);
            stat.setTotalDays(days);

            statsList.add(stat);
            totalDays += days;
        }

        Long approvedCount = requestRepo.countApprovedInMonth(employeeId, year, month);

        MonthlyStatsResponse response = new MonthlyStatsResponse();
        response.setEmployeeId(employeeId);
        response.setYear(year);
        response.setMonth(month);
        response.setTotalApprovedCount(approvedCount.intValue());
        response.setTotalDays(totalDays);
        response.setExceededLimit(approvedCount > 2);
        response.setBreakdown(statsList);

        return response;
    }

    /**
     * Manager Dashboard - All team members' balances
     */
    @Transactional(readOnly = true)
    public List<TeamMemberBalance> getTeamBalances(Long managerId, Integer year) {
        log.info("[DASHBOARD] Team balances for manager={}", managerId);

        List<User> team = userRepo.findEmployeesByManagerId(managerId);
        List<TeamMemberBalance> teamBalances = new ArrayList<>();

        for (User employee : team) {
            LeaveBalanceResponse balance = balanceService.getBalance(employee.getId(), year);

            TeamMemberBalance tmb = new TeamMemberBalance();
            tmb.setEmployeeId(employee.getId());
            tmb.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
            tmb.setTotalAllocated(balance.getTotalAllocated());
            tmb.setTotalUsed(balance.getTotalUsed());
            tmb.setTotalRemaining(balance.getTotalRemaining());
            tmb.setCompOffBalance(balance.getCompOffBalance());
            tmb.setLopPercentage(balance.getLopPercentage());
            tmb.setTotalWorkingDays(balance.getTotalWorkingDays());

            teamBalances.add(tmb);
        }

        return teamBalances;
    }

    /**
     * Manager Dashboard - Pending count
     */
    @Transactional(readOnly = true)
    public int getPendingCount(Long managerId) {
        return requestRepo.findPendingForManager(managerId).size();
    }
}


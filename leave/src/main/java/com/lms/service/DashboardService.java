package com.lms.service;

import com.lms.dto.response.*;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

    private final LeaveRequestRepository requestRepo;
    private final CompOffRequestRepository compOffRequestRepo;
    private final UserRepository userRepo;

    /**
     * ====================================================================
     * Get monthly statistics for employee/manager dashboard
     * ====================================================================
     */
    @Transactional(readOnly = true)
    public MonthlyStatsResponse getMonthlyStats(Long employeeId, Integer year, Integer month) {
        List<Object[]> stats = requestRepo.getMonthlyStats(employeeId, year, month);

        Map<String, MonthlyStatsResponse.LeaveTypeStat> statMap = new HashMap<>();
        int totalCount = 0;
        double totalDays = 0;

        for (Object[] row : stats) {
            String leaveType = row[0].toString();
            long count = ((Number) row[1]).longValue();
            double days = ((Number) row[2]).doubleValue();

            MonthlyStatsResponse.LeaveTypeStat stat = new MonthlyStatsResponse.LeaveTypeStat();
            stat.setLeaveType(leaveType);
            stat.setCount((int) count);
            stat.setTotalDays(days);

            statMap.put(leaveType, stat);
            totalCount += count;
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
        response.setBreakdown(new ArrayList<>(statMap.values()));

        return response;
    }

    /**
     * ====================================================================
     * Get pending counts for manager dashboard
     * ====================================================================
     */
    @Transactional(readOnly = true)
    public PendingApprovalsResponse getPendingApprovals(Long managerId) {
        int pendingLeaves = requestRepo.findPendingForManager(managerId).size();
        int pendingCompOffs = compOffRequestRepo.findPendingForManager(managerId).size();

        PendingApprovalsResponse response = new PendingApprovalsResponse();
        response.setPendingCount(pendingLeaves + pendingCompOffs);
        response.setPendingLeaves(pendingLeaves);
        response.setPendingCompOffs(pendingCompOffs);

        return response;
    }
}
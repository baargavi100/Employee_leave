package com.lms.dto.response;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyStatsResponse {
    private Long employeeId;
    private Integer year;
    private Integer month;
    private Integer totalApprovedCount;
    private Double totalDays;
    private Boolean exceededLimit;
    private List<LeaveTypeStat> breakdown;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeaveTypeStat {
        private String leaveType;
        private Integer count;
        private Double totalDays;
    }
}

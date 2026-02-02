package com.lms.dto.request;

import com.lms.enums.HalfDayType;
import com.lms.enums.LeaveCategory;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveApplyRequest {

    private Long employeeId;
    private LeaveCategory leaveCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalDays;        // 0.5 for half-day
    private HalfDayType halfDayType; // FIRST_HALF / SECOND_HALF / FULL_DAY
    private String reason;
}

package com.lms.dto.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveApplicationRequest {
    private Long employeeId;
    private String leaveCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalDays;
    private String halfDayType;
    private String reason;
    private Boolean usesCompOff;
    private Double compOffDaysUsed;
}
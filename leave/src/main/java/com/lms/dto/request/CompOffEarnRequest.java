package com.lms.dto.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CompOffEarnRequest {
    private Long employeeId;
    private LocalDate holidayWorkDate;
    private String reason;
}
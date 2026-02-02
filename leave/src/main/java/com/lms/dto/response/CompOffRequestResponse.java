package com.lms.dto.response;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompOffRequestResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate holidayWorkDate;
    private String reason;
    private Integer daysEarned;
    private String status;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}
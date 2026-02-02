package com.lms.dto.request;

import lombok.Data;

@Data
public class LeaveRejectionRequest {

    private Long managerId;
    private String reason;
}

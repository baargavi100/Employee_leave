package com.lms.dto.response;

import lombok.Data;

@Data
public class PendingApprovalsResponse {

    private int pendingCount;
    private int thisMonth;
    private int thisWeek;
}

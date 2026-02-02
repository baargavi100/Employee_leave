package com.lms.dto.response;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingApprovalsResponse {
    private Integer pendingCount;
    private Integer pendingLeaves;
    private Integer pendingCompOffs;
}
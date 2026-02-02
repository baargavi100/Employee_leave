package com.lms.service;

import com.lms.dto.response.LeaveHistoryResponse;
import com.lms.entity.LeaveHistory;
import com.lms.entity.LeaveRequest;
import com.lms.entity.User;
import com.lms.enums.LeaveStatus;
import com.lms.repository.LeaveHistoryRepository;
import com.lms.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveHistoryService {

    private final LeaveHistoryRepository leaveHistoryRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    /**
     * 🔥 FIXES YOUR ERROR
     * RECORD APPROVAL / REJECTION AUDIT
     */
    public void recordStatusChange(
            LeaveRequest leave,
            LeaveStatus previousStatus,
            LeaveStatus newStatus,
            User changedBy,
            String notes
    ) {
        LeaveHistory history = new LeaveHistory();
        history.setLeaveRequest(leave);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(changedBy);
        history.setChangedAt(LocalDateTime.now());
        history.setNotes(notes);

        leaveHistoryRepository.save(history);
    }

    /**
     * GET EMPLOYEE LEAVE HISTORY
     */
    public List<LeaveHistoryResponse> getEmployeeHistory(Long employeeId) {

        return leaveRequestRepository
                .findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(leave -> {
                    LeaveHistoryResponse r = new LeaveHistoryResponse();
                    r.setLeaveId(leave.getId());
                    r.setLeaveType(leave.getLeaveCategory().name());
                    r.setStartDate(leave.getStartDate());
                    r.setEndDate(leave.getEndDate());
                    r.setTotalDays(leave.getTotalDays());
                    r.setStatus(leave.getStatus().name());
                    r.setAppliedBy(
                            leave.getEmployee().getFirstName()
                                    + " "
                                    + leave.getEmployee().getLastName()
                    );
                    r.setAppliedAt(leave.getCreatedAt());
                    r.setReason(leave.getReason());
                    return r;
                })
                .collect(Collectors.toList());
    }
}

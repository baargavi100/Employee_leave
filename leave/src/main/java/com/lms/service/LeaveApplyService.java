package com.lms.service;

import com.lms.dto.request.LeaveApplyRequest;
import com.lms.entity.LeaveRequest;
import com.lms.entity.User;
import com.lms.enums.LeaveStatus;
import com.lms.repository.LeaveRequestRepository;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveApplyService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public LeaveRequest applyLeave(LeaveApplyRequest request) {

        User employee = userRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setLeaveCategory(request.getLeaveCategory());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setTotalDays(request.getTotalDays());
        leave.setHalfDayType(request.getHalfDayType());
        leave.setReason(request.getReason());
        leave.setStatus(LeaveStatus.PENDING);

        return leaveRequestRepository.save(leave);
    }
}

package com.lms.service;

import com.lms.entity.LeaveType;
import com.lms.enums.LeaveCategory;
import com.lms.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveType createLeaveType(LeaveType leaveType) {
        if (leaveTypeRepository.existsByCategory(leaveType.getCategory())) {
            throw new RuntimeException("Leave type already exists");
        }
        return leaveTypeRepository.save(leaveType);
    }

    public LeaveType getByCategory(LeaveCategory category) {
        return leaveTypeRepository.findByCategory(category)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
    }

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }
}

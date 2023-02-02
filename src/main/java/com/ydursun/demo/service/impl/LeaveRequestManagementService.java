package com.ydursun.demo.service.impl;

import com.ydursun.demo.dto.PagingDto;
import com.ydursun.demo.dto.response.LeaveRequestDetailResponse;
import com.ydursun.demo.dto.response.LeaveRequestResponse;
import com.ydursun.demo.exception.LeaveRequestNotFoundException;
import com.ydursun.demo.exception.TokenUserNotFoundException;
import com.ydursun.demo.model.converter.LeaveRequestEntityToLeaveRequestDetailResponseConverter;
import com.ydursun.demo.model.converter.LeaveRequestEntityToLeaveRequestResponseConverter;
import com.ydursun.demo.model.entity.LeaveRequestEntity;
import com.ydursun.demo.model.entity.UserEntity;
import com.ydursun.demo.model.enums.LeaveRequestStatus;
import com.ydursun.demo.repository.LeaveRequestRepository;
import com.ydursun.demo.service.UserManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestManagementService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserManagementService userManagementService;
    private final LeaveRequestEntityToLeaveRequestResponseConverter leaveRequestEntityToLeaveRequestResponseConverter;
    private final LeaveRequestEntityToLeaveRequestDetailResponseConverter leaveRequestEntityToLeaveRequestDetailResponseConverter;
    public LeaveRequestManagementService(LeaveRequestRepository leaveRequestRepository,
                                         UserManagementService userManagementService,
                                         LeaveRequestEntityToLeaveRequestResponseConverter leaveRequestEntityToLeaveRequestResponseConverter,
                                         LeaveRequestEntityToLeaveRequestDetailResponseConverter leaveRequestEntityToLeaveRequestDetailResponseConverter) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userManagementService = userManagementService;
        this.leaveRequestEntityToLeaveRequestResponseConverter = leaveRequestEntityToLeaveRequestResponseConverter;
        this.leaveRequestEntityToLeaveRequestDetailResponseConverter = leaveRequestEntityToLeaveRequestDetailResponseConverter;
    }

    public PagingDto<LeaveRequestDetailResponse> getAllLeaveRequests(Pageable pageable) {
        Page<LeaveRequestEntity> leaveRequestEntityPage = leaveRequestRepository.findAllLeaveRequestEntityList(pageable);
        List<LeaveRequestDetailResponse> leaveRequestResponsePage = leaveRequestEntityPage.stream()
                .map(leaveRequestEntityToLeaveRequestDetailResponseConverter::convert)
                .collect(Collectors.toList());

        return new PagingDto<>(pageable.getPageNumber(),
                pageable.getPageSize(),
                leaveRequestEntityPage.getTotalElements(),
                leaveRequestResponsePage);
    }

    public LeaveRequestResponse operate(long leaveRequestId, LeaveRequestStatus leaveRequestStatus, String token) {
        Optional<UserEntity> userEntityOptional = userManagementService.getUserFromHeaderToken(token);
        if(!userEntityOptional.isPresent()) {
            throw new TokenUserNotFoundException();
        }

        Optional<LeaveRequestEntity> leaveRequestEntityOptional = leaveRequestRepository.findById(leaveRequestId);
        if(!leaveRequestEntityOptional.isPresent()) {
            throw new LeaveRequestNotFoundException();
        }
        LeaveRequestEntity leaveRequestEntity = leaveRequestEntityOptional.get();
        leaveRequestEntity.setStatus(leaveRequestStatus);
        leaveRequestEntity.setUserApprover(userEntityOptional.get());
        leaveRequestEntity.setApprovedTime(new Date());

        return leaveRequestEntityToLeaveRequestResponseConverter.convert(leaveRequestRepository.save(leaveRequestEntity));
    }

}

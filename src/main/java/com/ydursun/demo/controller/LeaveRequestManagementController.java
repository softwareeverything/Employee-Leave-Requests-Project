package com.ydursun.demo.controller;

import com.ydursun.demo.dto.PagingDto;
import com.ydursun.demo.dto.request.OperateLeaveRequestRequest;
import com.ydursun.demo.dto.response.EndpointResponse;
import com.ydursun.demo.dto.response.LeaveRequestDetailResponse;
import com.ydursun.demo.dto.response.LeaveRequestResponse;
import com.ydursun.demo.service.impl.LeaveRequestManagementService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/live-request-management")
public class LeaveRequestManagementController {

    private final LeaveRequestManagementService leaveRequestManagementService;

    public LeaveRequestManagementController(LeaveRequestManagementService leaveRequestManagementService) {
        this.leaveRequestManagementService = leaveRequestManagementService;
    }

    @GetMapping
    public ResponseEntity<EndpointResponse<PagingDto<LeaveRequestDetailResponse>>> getAllLeaveRequests(Pageable pageable) {
        PagingDto<LeaveRequestDetailResponse> response = leaveRequestManagementService.getAllLeaveRequests(pageable);
        int httpStatus = HttpStatus.OK.value();
        return ResponseEntity.status(httpStatus).body(new EndpointResponse<>(httpStatus,
                null,
                "",
                response));
    }

    @PostMapping("{leave-request-id}/operate")
    public ResponseEntity<EndpointResponse<LeaveRequestResponse>> operate(@PathVariable("leave-request-id") long leaveRequestId,
                                                                          @RequestBody @Valid OperateLeaveRequestRequest request,
                                                                          @RequestHeader(name = "Authorization") String token) {
        LeaveRequestResponse response = leaveRequestManagementService.operate(leaveRequestId, request.getLeaveRequestStatus(), token);
        int httpStatus = HttpStatus.OK.value();
        return ResponseEntity.status(httpStatus).body(new EndpointResponse<>(httpStatus,
                null,
                "",
                response));
    }

}

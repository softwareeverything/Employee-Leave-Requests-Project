package com.ydursun.demo.controller;

import com.ydursun.demo.dto.PagingDto;
import com.ydursun.demo.dto.request.CreateLeaveRequestRequest;
import com.ydursun.demo.dto.response.EndpointResponse;
import com.ydursun.demo.dto.response.ErrorResponse;
import com.ydursun.demo.dto.response.LeaveRequestResponse;
import com.ydursun.demo.exception.RequestedDaysBiggerRemainingDaysException;
import com.ydursun.demo.exception.SpecifiedDaysAlreadyDaysOffException;
import com.ydursun.demo.exception.TokenUserNotFoundException;
import com.ydursun.demo.exception.UserAlreadyHasAWaitingLeaveRequestException;
import com.ydursun.demo.service.impl.LeaveRequestService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/live-request")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping("/create")
    public ResponseEntity<EndpointResponse<LeaveRequestResponse>> create(@RequestBody @Valid CreateLeaveRequestRequest request,
                                                                         @RequestHeader(name="Authorization") String token) {
        LeaveRequestResponse response = leaveRequestService.create(request, token);
        int httpStatus = HttpStatus.CREATED.value();
        return ResponseEntity.status(httpStatus).body(new EndpointResponse<>(httpStatus,
                null,
                "",
                response));
    }

    @GetMapping
    public ResponseEntity<EndpointResponse<PagingDto<LeaveRequestResponse>>> getAllLeaveRequestsByUser(@RequestHeader(name="Authorization") String token, Pageable pageable) {
        PagingDto<LeaveRequestResponse> response = leaveRequestService.getAllLeaveRequestsByUser(token, pageable);
        int httpStatus = HttpStatus.CREATED.value();
        return ResponseEntity.status(httpStatus).body(new EndpointResponse<>(httpStatus,
                null,
                "",
                response));
    }

    @ExceptionHandler(TokenUserNotFoundException.class)
    public ResponseEntity<EndpointResponse<ErrorResponse>> handleTokenUserNotFoundException(TokenUserNotFoundException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(UserAlreadyHasAWaitingLeaveRequestException.class)
    public ResponseEntity<EndpointResponse<ErrorResponse>> handleUserAlreadyHasAWaitingLeaveRequestException(UserAlreadyHasAWaitingLeaveRequestException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(SpecifiedDaysAlreadyDaysOffException.class)
    public ResponseEntity<EndpointResponse<ErrorResponse>> handleSpecifiedDaysAlreadyDaysOffExceptionException(SpecifiedDaysAlreadyDaysOffException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(RequestedDaysBiggerRemainingDaysException.class)
    public ResponseEntity<EndpointResponse<ErrorResponse>> handleRequestedDaysBiggerRemainingDaysException(RequestedDaysBiggerRemainingDaysException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex);
    }

    private ResponseEntity<EndpointResponse<ErrorResponse>> handleException(HttpStatus httpStatus, Exception ex) {
        ErrorResponse response = new ErrorResponse();
        return ResponseEntity.status(httpStatus).body(new EndpointResponse<>(httpStatus.value(),
                ex.getMessage(),
                "",
                response));
    }

}

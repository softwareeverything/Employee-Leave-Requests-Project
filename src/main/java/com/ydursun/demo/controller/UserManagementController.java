package com.ydursun.demo.controller;

import com.ydursun.demo.dto.request.CreateUserRequest;
import com.ydursun.demo.dto.response.EndpointResponse;
import com.ydursun.demo.dto.response.UserDetailResponse;
import com.ydursun.demo.service.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {

    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("/create")
    public ResponseEntity<EndpointResponse<UserDetailResponse>> create(@RequestBody @Valid CreateUserRequest request) {
        UserDetailResponse response = userManagementService.create(request);
        int httpStatus = HttpStatus.CREATED.value();
        return ResponseEntity.status(httpStatus).body(new EndpointResponse<>(httpStatus,
                null,
                "",
                response));
    }

}

package com.ydursun.demo.service;

import com.ydursun.demo.dto.request.CreateUserRequest;
import com.ydursun.demo.dto.response.UserDetailResponse;
import com.ydursun.demo.exception.GeneralException;
import com.ydursun.demo.model.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserManagementService extends UserDetailsService {

    UserDetailResponse create(CreateUserRequest request);

    Optional<UserEntity> getUserFromHeaderToken(String tokenHeader);

}

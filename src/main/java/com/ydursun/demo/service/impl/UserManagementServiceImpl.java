package com.ydursun.demo.service.impl;

import com.ydursun.demo.config.Translator;
import com.ydursun.demo.dto.request.CreateUserRequest;
import com.ydursun.demo.dto.response.UserDetailResponse;
import com.ydursun.demo.exception.GeneralException;
import com.ydursun.demo.exception.UserAlreadyExistsException;
import com.ydursun.demo.model.converter.UserEntityToUserDetailResponseConverter;
import com.ydursun.demo.model.entity.UserEntity;
import com.ydursun.demo.repository.UserRepository;
import com.ydursun.demo.service.TokenService;
import com.ydursun.demo.service.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final UserEntityToUserDetailResponseConverter userEntityToUserDetailResponseConverter;

    public UserManagementServiceImpl(PasswordEncoder passwordEncoder,
                                     UserRepository userRepository,
                                     TokenService tokenService,
                                     UserEntityToUserDetailResponseConverter userEntityToUserDetailResponseConverter) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userEntityToUserDetailResponseConverter = userEntityToUserDetailResponseConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userDaoOptional = userRepository.findByEmail(email);
        if (userDaoOptional.isPresent()) {
            UserEntity user = userDaoOptional.get();
            List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
            return new User(user.getEmail(), user.getPassword(), roles);
        }
        throw new UsernameNotFoundException("User not found: " + email);
    }

    @Override
    public UserDetailResponse create(CreateUserRequest request) {
        Optional<UserEntity> userOld = userRepository.findByEmail(request.getEmail());
        if (userOld.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        UserEntity userNew = new UserEntity();
        userNew.setEmail(request.getEmail());
        userNew.setPassword(passwordEncoder.encode(request.getPassword()));
        userNew.setRole(request.getRole());
        userNew.setStartedAt(request.getStartedAt());

        return userEntityToUserDetailResponseConverter.convert(userRepository.save(userNew));
    }

    @Override
    public Optional<UserEntity> getUserFromHeaderToken(String tokenHeader) {
        Optional<String> tokenOptional = tokenService.getTokenFromHeaderToken(tokenHeader);
        if(tokenOptional.isPresent()) {
            Optional<String> emailOptional = tokenService.getUserEmailFromAccessToken(tokenOptional.get());
            if(emailOptional.isPresent()) {
                return userRepository.findByEmail(emailOptional.get());
            }
        }
        return Optional.empty();
    }
}

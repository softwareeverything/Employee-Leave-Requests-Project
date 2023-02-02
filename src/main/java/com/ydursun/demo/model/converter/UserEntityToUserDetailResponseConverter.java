package com.ydursun.demo.model.converter;

import com.ydursun.demo.dto.response.UserDetailResponse;
import com.ydursun.demo.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToUserDetailResponseConverter implements GenericConverter<UserEntity, UserDetailResponse> {

    @Override
    public UserDetailResponse convert(UserEntity source) {
        if(source == null) {
            return null;
        }

        UserDetailResponse target = new UserDetailResponse();
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setName(source.getName());
        target.setLastname(source.getLastname());
        target.setRole(source.getRole());
        target.setStatus(source.getStatus());
        target.setStartedAt(source.getStartedAt());
        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedAt(source.getCreatedAt());

        return target;
    }

}
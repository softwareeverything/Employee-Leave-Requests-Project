package com.ydursun.demo.model.converter;

import com.ydursun.demo.dto.response.LeaveRequestDetailResponse;
import com.ydursun.demo.dto.response.LeaveRequestResponse;
import com.ydursun.demo.model.entity.LeaveRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestEntityToLeaveRequestDetailResponseConverter implements GenericConverter<LeaveRequestEntity, LeaveRequestDetailResponse> {

    private final UserEntityToUserDetailResponseConverter userEntityToUserDetailResponseConverter;

    public LeaveRequestEntityToLeaveRequestDetailResponseConverter(UserEntityToUserDetailResponseConverter userEntityToUserDetailResponseConverter) {
        this.userEntityToUserDetailResponseConverter = userEntityToUserDetailResponseConverter;
    }

    @Override
    public LeaveRequestDetailResponse convert(LeaveRequestEntity source) {
        if(source == null) {
            return null;
        }

        LeaveRequestDetailResponse target = new LeaveRequestDetailResponse();
        target.setId(source.getId());
        target.setUserRequester(userEntityToUserDetailResponseConverter.convert(source.getUserRequester()));
        target.setUserApprover(userEntityToUserDetailResponseConverter.convert(source.getUserApprover()));
        target.setDescription(source.getDescription());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
        target.setDaysOff(source.getDaysOff());
        target.setApprovedTime(source.getApprovedTime());
        target.setStatus(source.getStatus());
        target.setCreatedAt(source.getCreatedAt());

        return target;
    }

}
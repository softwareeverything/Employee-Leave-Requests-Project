package com.ydursun.demo.model.converter;

import com.ydursun.demo.dto.request.CreateLeaveRequestRequest;
import com.ydursun.demo.model.entity.LeaveRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class CreateLeaveRequestRequestToLeaveRequestEntityConverter implements GenericConverter<CreateLeaveRequestRequest, LeaveRequestEntity> {

    @Override
    public LeaveRequestEntity convert(CreateLeaveRequestRequest source) {
        if(source == null) {
            return null;
        }

        LeaveRequestEntity entity = new LeaveRequestEntity();
        entity.setDescription(source.getDescription());
        entity.setStartDate(source.getStartDate());
        entity.setEndDate(source.getEndDate());

        return entity;
    }

}
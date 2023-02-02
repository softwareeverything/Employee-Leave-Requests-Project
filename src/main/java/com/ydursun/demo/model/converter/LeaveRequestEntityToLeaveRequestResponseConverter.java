package com.ydursun.demo.model.converter;

import com.ydursun.demo.dto.response.LeaveRequestResponse;
import com.ydursun.demo.model.entity.LeaveRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestEntityToLeaveRequestResponseConverter implements GenericConverter<LeaveRequestEntity, LeaveRequestResponse> {

    @Override
    public LeaveRequestResponse convert(LeaveRequestEntity source) {
        if(source == null) {
            return null;
        }

        LeaveRequestResponse target = new LeaveRequestResponse();
        target.setId(source.getId());
        target.setDescription(source.getDescription());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
        target.setStatus(source.getStatus());
        target.setCreatedAt(source.getCreatedAt());

        return target;
    }

}
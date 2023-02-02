package com.ydursun.demo.dto.request;

import com.ydursun.demo.model.enums.LeaveRequestStatus;

public class OperateLeaveRequestRequest {

    private LeaveRequestStatus leaveRequestStatus;

    public LeaveRequestStatus getLeaveRequestStatus() {
        return leaveRequestStatus;
    }

    public void setLeaveRequestStatus(LeaveRequestStatus leaveRequestStatus) {
        this.leaveRequestStatus = leaveRequestStatus;
    }

}

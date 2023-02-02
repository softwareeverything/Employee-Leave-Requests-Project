package com.ydursun.demo.dto.response;

import com.ydursun.demo.dto.BaseDto;
import com.ydursun.demo.model.enums.LeaveRequestStatus;

import java.util.Date;

public class LeaveRequestDetailResponse extends BaseDto {

    private long id;

    private UserDetailResponse userRequester;
    private UserDetailResponse userApprover;

    private String description;

    private Date startDate;

    private Date endDate;
    private int daysOff;

    private Date approvedTime;

    private LeaveRequestStatus status;

    private Date createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDetailResponse getUserRequester() {
        return userRequester;
    }

    public void setUserRequester(UserDetailResponse userRequester) {
        this.userRequester = userRequester;
    }

    public UserDetailResponse getUserApprover() {
        return userApprover;
    }

    public void setUserApprover(UserDetailResponse userApprover) {
        this.userApprover = userApprover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(int daysOff) {
        this.daysOff = daysOff;
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    public LeaveRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveRequestStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

package com.ydursun.demo.model.entity;

import com.ydursun.demo.model.enums.LeaveRequestStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "leave_request")
public class LeaveRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private UserEntity userRequester;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private UserEntity userApprover;

    @Column
    private String description;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private int daysOff;

    @Column
    private Date approvedTime;

    @Column
    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus status = LeaveRequestStatus.WAITING;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public UserEntity getUserRequester() {
        return userRequester;
    }

    public void setUserRequester(UserEntity userRequester) {
        this.userRequester = userRequester;
    }

    public UserEntity getUserApprover() {
        return userApprover;
    }

    public void setUserApprover(UserEntity userApprover) {
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

}
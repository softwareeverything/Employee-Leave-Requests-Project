package com.ydursun.demo.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;

public class CreateLeaveRequestRequest {

    @NotEmpty(message = "{validation.description.not-empty}")
    @Size(max = 600, message = "{validation.description.max}")
    private String description;

    @NotNull(message = "{validation.dates.not-null}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;

    @NotNull(message = "{validation.dates.not-null}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;

    @AssertTrue(message = "{validation.dates.wrong-dates-order}")
    private boolean isDatesAccepted() {
        return startDate.before(endDate) || startDate.equals(endDate);
    }

    @AssertTrue(message = "{validation.dates.must-be-present-or-future}")
    private boolean isStartDateAccepted() {
        Calendar calendarYesterday = Calendar.getInstance();
        calendarYesterday.add(Calendar.DAY_OF_MONTH, -1);

        Calendar calendarStarting = Calendar.getInstance();
        calendarStarting.setTime(startDate);

        return calendarYesterday.before(calendarStarting);
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
}

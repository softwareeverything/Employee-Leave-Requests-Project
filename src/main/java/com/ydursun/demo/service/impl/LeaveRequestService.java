package com.ydursun.demo.service.impl;

import com.ydursun.demo.dto.PagingDto;
import com.ydursun.demo.dto.request.CreateLeaveRequestRequest;
import com.ydursun.demo.dto.response.LeaveRequestResponse;
import com.ydursun.demo.exception.RequestedDaysBiggerRemainingDaysException;
import com.ydursun.demo.exception.SpecifiedDaysAlreadyDaysOffException;
import com.ydursun.demo.exception.TokenUserNotFoundException;
import com.ydursun.demo.exception.UserAlreadyHasAWaitingLeaveRequestException;
import com.ydursun.demo.model.converter.CreateLeaveRequestRequestToLeaveRequestEntityConverter;
import com.ydursun.demo.model.converter.LeaveRequestEntityToLeaveRequestResponseConverter;
import com.ydursun.demo.model.entity.LeaveRequestEntity;
import com.ydursun.demo.model.entity.UserEntity;
import com.ydursun.demo.repository.LeaveRequestRepository;
import com.ydursun.demo.service.UserManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final CreateLeaveRequestRequestToLeaveRequestEntityConverter createLeaveRequestRequestToLeaveRequestEntityConverter;
    private final LeaveRequestEntityToLeaveRequestResponseConverter leaveRequestEntityToLeaveRequestResponseConverter;
    private final HolidayService holidayService;
    private final UserManagementService userManagementService;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               CreateLeaveRequestRequestToLeaveRequestEntityConverter createLeaveRequestRequestToLeaveRequestEntityConverter,
                               LeaveRequestEntityToLeaveRequestResponseConverter leaveRequestEntityToLeaveRequestResponseConverter,
                               HolidayService holidayService,
                               UserManagementService userManagementService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.createLeaveRequestRequestToLeaveRequestEntityConverter = createLeaveRequestRequestToLeaveRequestEntityConverter;
        this.leaveRequestEntityToLeaveRequestResponseConverter = leaveRequestEntityToLeaveRequestResponseConverter;
        this.holidayService = holidayService;
        this.userManagementService = userManagementService;
    }

    public LeaveRequestResponse create(CreateLeaveRequestRequest request, String token) {
        LeaveRequestEntity leaveRequestEntity = createLeaveRequestRequestToLeaveRequestEntityConverter.convert(request);

        UserEntity userEntity = getUserFromHeaderToken(token);
        leaveRequestEntity.setUserRequester(userEntity);

        List<LeaveRequestEntity> userWaitingLeaveRequests = leaveRequestRepository.findWaitingLeaveRequestEntityListByRequesterEmail(userEntity.getEmail());
        if(!userWaitingLeaveRequests.isEmpty()) {
            throw new UserAlreadyHasAWaitingLeaveRequestException();
        }

        int sumDaysOffSinceLastYear = 0;
        Optional<Integer> sumDaysOffSinceLastYearOptional = leaveRequestRepository.sumApprovedDaysOffSinceLastYearByRequesterId(userEntity.getId());
        if(sumDaysOffSinceLastYearOptional.isPresent()) {
            sumDaysOffSinceLastYear = sumDaysOffSinceLastYearOptional.get();
        }

        List<Calendar> holidayCalendarList = holidayService.getAllHolidaysAsCalendarList();
        int daysRequested = getWorkingDaysBetweenTwoDatesAndExceptHolidays(request.getStartDate(), request.getEndDate(), holidayCalendarList);
        leaveRequestEntity.setDaysOff(daysRequested);
        if(daysRequested < 1) {
            throw new SpecifiedDaysAlreadyDaysOffException();
        }

        int daysRemaining = getDaysRemaining(userEntity.getStartedAt(), sumDaysOffSinceLastYear);
        if(daysRequested > daysRemaining) {
            throw new RequestedDaysBiggerRemainingDaysException();
        }

        return leaveRequestEntityToLeaveRequestResponseConverter.convert(leaveRequestRepository.save(leaveRequestEntity));
    }

    public PagingDto<LeaveRequestResponse> getAllLeaveRequestsByUser(String token, Pageable pageable) {
        UserEntity userEntity = getUserFromHeaderToken(token);

        Page<LeaveRequestEntity> leaveRequestEntityPage = leaveRequestRepository.findAllLeaveRequestEntityListByRequesterEmail(userEntity.getEmail(), pageable);
        List<LeaveRequestResponse> leaveRequestResponsePage = leaveRequestEntityPage.stream()
                .map(leaveRequestEntityToLeaveRequestResponseConverter::convert)
                .collect(Collectors.toList());

        return new PagingDto<>(pageable.getPageNumber(),
                pageable.getPageSize(),
                leaveRequestEntityPage.getTotalElements(),
                leaveRequestResponsePage);
    }

    public UserEntity getUserFromHeaderToken(String token) {
        Optional<UserEntity> userEntityOptional = userManagementService.getUserFromHeaderToken(token);
        if(!userEntityOptional.isPresent()) {
            throw new TokenUserNotFoundException();
        }
        return userEntityOptional.get();
    }

    public int getWorkingDaysBetweenTwoDatesAndExceptHolidays(Date startDate,
                                                               Date endDate,
                                                               List<Calendar> holidayCalendarList) {
        Calendar calendarStarting = Calendar.getInstance();
        calendarStarting.setTime(startDate);

        Calendar calendarEnding = Calendar.getInstance();
        calendarEnding.setTime(endDate);

        int workDays = 0;

        if (calendarStarting.getTimeInMillis() > calendarEnding.getTimeInMillis()) {
            calendarStarting.setTime(endDate);
            calendarEnding.setTime(startDate);
        }

        do {
            Optional<Calendar> collusionWithHoliday = holidayCalendarList.stream().filter(calendar ->
                            calendarStarting.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                                    && calendarStarting.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
                    .findFirst();

            if (!collusionWithHoliday.isPresent()
                    && calendarStarting.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && calendarStarting.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                workDays += 1;
            }
            calendarStarting.add(Calendar.DAY_OF_MONTH, 1);
        } while (calendarStarting.getTimeInMillis() <= calendarEnding.getTimeInMillis());

        return workDays;
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public int getDaysRemaining(Date startedAtWork, int daysUsedBefore) {
        long diff = getDateDiff(startedAtWork, new Date(), TimeUnit.DAYS);
        int employeYears = Math.round(diff/365f);

        int totalDaysRight;
        if(employeYears < 1) {
            totalDaysRight = 5;
        } else if (employeYears < 6) {
            totalDaysRight = 15;
        } else if (employeYears < 11) {
            totalDaysRight = 18;
        } else {
            totalDaysRight = 24;
        }

        return totalDaysRight - daysUsedBefore;
    }

}

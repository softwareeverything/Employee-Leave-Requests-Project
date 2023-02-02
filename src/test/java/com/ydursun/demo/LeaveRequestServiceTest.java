package com.ydursun.demo;

import com.ydursun.demo.base.BaseServiceTest;
import com.ydursun.demo.config.Translator;
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
import com.ydursun.demo.model.enums.Role;
import com.ydursun.demo.model.enums.UserStatus;
import com.ydursun.demo.repository.LeaveRequestRepository;
import com.ydursun.demo.service.UserManagementService;
import com.ydursun.demo.service.impl.HolidayService;
import com.ydursun.demo.service.impl.LeaveRequestService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class LeaveRequestServiceTest extends BaseServiceTest {


    private LeaveRequestService leaveRequestService;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private CreateLeaveRequestRequestToLeaveRequestEntityConverter createLeaveRequestRequestToLeaveRequestEntityConverter;

    @Mock
    private LeaveRequestEntityToLeaveRequestResponseConverter leaveRequestEntityToLeaveRequestResponseConverter;

    @Mock
    private HolidayService holidayService;

    @Mock
    private UserManagementService userManagementService;

    @Before
    public void firstSetup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setup() {
        this.leaveRequestService = new LeaveRequestService(leaveRequestRepository,
                createLeaveRequestRequestToLeaveRequestEntityConverter,
                leaveRequestEntityToLeaveRequestResponseConverter,
                holidayService,
                userManagementService);
    }

    @Test
    void shouldCreateLeaveRequest() {
        Calendar calendar = Calendar.getInstance();
        Date dateNow = calendar.getTime();
        String description = "New Leave Request";

        CreateLeaveRequestRequest request = new CreateLeaveRequestRequest();
        request.setDescription(description);
        request.setStartDate(dateNow);
        request.setEndDate(dateNow);

        calendar.add(Calendar.YEAR, -1);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setRole(Role.USER);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setStartedAt(calendar.getTime());

        LeaveRequestEntity leaveRequestEntity = new LeaveRequestEntity();
        leaveRequestEntity.setDescription(description);
        leaveRequestEntity.setStartDate(dateNow);
        leaveRequestEntity.setEndDate(dateNow);
        leaveRequestEntity.setUserRequester(userEntity);
        leaveRequestEntity.setDaysOff(1);

        LeaveRequestResponse response = new LeaveRequestResponse();
        response.setId(1L);
        response.setDescription(description);
        response.setStartDate(dateNow);
        response.setEndDate(dateNow);

        when(createLeaveRequestRequestToLeaveRequestEntityConverter.convert(any())).thenReturn(leaveRequestEntity);
        when(userManagementService.getUserFromHeaderToken(anyString())).thenReturn(Optional.of(userEntity));
        when(leaveRequestRepository.findWaitingLeaveRequestEntityListByRequesterEmail(any())).thenReturn(Collections.emptyList());
        when(leaveRequestRepository.sumApprovedDaysOffSinceLastYearByRequesterId(any())).thenReturn(Optional.of(0));
        when(holidayService.getAllHolidaysAsCalendarList()).thenReturn(Collections.emptyList());
        when(leaveRequestRepository.save(any())).thenReturn(leaveRequestEntity);
        when(leaveRequestEntityToLeaveRequestResponseConverter.convert(leaveRequestEntity)).thenReturn(response);

        LeaveRequestResponse result = leaveRequestService.create(request, "");

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldNotCreateLeaveRequest_whenUserNotFound() {
        loadMessageSource();

        Calendar calendar = Calendar.getInstance();
        Date dateNow = calendar.getTime();
        String description = "New Leave Request";

        CreateLeaveRequestRequest request = new CreateLeaveRequestRequest();
        request.setDescription(description);
        request.setStartDate(dateNow);
        request.setEndDate(dateNow);

        assertThatExceptionOfType(TokenUserNotFoundException.class).isThrownBy(() -> {
            leaveRequestService.create(request, "");
        });
    }

    @Test
    void shouldNotCreateLeaveRequest_whenUserAlreadyHasAWaitingLeaveRequest() {
        loadMessageSource();

        Calendar calendar = Calendar.getInstance();
        Date dateNow = calendar.getTime();
        String description = "New Leave Request";

        CreateLeaveRequestRequest request = new CreateLeaveRequestRequest();
        request.setDescription(description);
        request.setStartDate(dateNow);
        request.setEndDate(dateNow);

        calendar.add(Calendar.YEAR, -1);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setRole(Role.USER);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setStartedAt(calendar.getTime());

        LeaveRequestEntity leaveRequestEntity = new LeaveRequestEntity();
        leaveRequestEntity.setDescription(description);
        leaveRequestEntity.setStartDate(dateNow);
        leaveRequestEntity.setEndDate(dateNow);
        leaveRequestEntity.setUserRequester(userEntity);
        leaveRequestEntity.setDaysOff(1);

        when(createLeaveRequestRequestToLeaveRequestEntityConverter.convert(any())).thenReturn(leaveRequestEntity);
        when(userManagementService.getUserFromHeaderToken(anyString())).thenReturn(Optional.of(userEntity));
        when(leaveRequestRepository.findWaitingLeaveRequestEntityListByRequesterEmail(any())).thenReturn(Collections.singletonList(leaveRequestEntity));

        assertThatExceptionOfType(UserAlreadyHasAWaitingLeaveRequestException.class).isThrownBy(() -> {
            leaveRequestService.create(request, "");
        });
    }

    @Test
    void shouldNotCreateLeaveRequest_whenRequestedDaysLessThanOneWithHolidays() {
        loadMessageSource();

        Calendar calendar = Calendar.getInstance();
        Date dateNow = calendar.getTime();
        String description = "New Leave Request";

        CreateLeaveRequestRequest request = new CreateLeaveRequestRequest();
        request.setDescription(description);
        request.setStartDate(dateNow);
        request.setEndDate(dateNow);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setRole(Role.USER);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setStartedAt(calendar.getTime());

        LeaveRequestEntity leaveRequestEntity = new LeaveRequestEntity();
        leaveRequestEntity.setDescription(description);
        leaveRequestEntity.setStartDate(dateNow);
        leaveRequestEntity.setEndDate(dateNow);
        leaveRequestEntity.setUserRequester(userEntity);
        leaveRequestEntity.setDaysOff(1);

        when(createLeaveRequestRequestToLeaveRequestEntityConverter.convert(any())).thenReturn(leaveRequestEntity);
        when(userManagementService.getUserFromHeaderToken(anyString())).thenReturn(Optional.of(userEntity));
        when(leaveRequestRepository.findWaitingLeaveRequestEntityListByRequesterEmail(any())).thenReturn(Collections.emptyList());
        when(leaveRequestRepository.sumApprovedDaysOffSinceLastYearByRequesterId(any())).thenReturn(Optional.of(0));
        when(holidayService.getAllHolidaysAsCalendarList()).thenReturn(Collections.singletonList(calendar));

        assertThatExceptionOfType(SpecifiedDaysAlreadyDaysOffException.class).isThrownBy(() -> {
            leaveRequestService.create(request, "");
        });
    }

    @Test
    void shouldNotCreateLeaveRequest_whenRequestedDaysGreaterThanRemainingDays() {
        loadMessageSource();

        Calendar calendar = Calendar.getInstance();
        Date dateNow = calendar.getTime();
        String description = "New Leave Request";

        calendar.add(Calendar.YEAR, 3);
        CreateLeaveRequestRequest request = new CreateLeaveRequestRequest();
        request.setDescription(description);
        request.setStartDate(dateNow);
        request.setEndDate(calendar.getTime());

        calendar.add(Calendar.YEAR, -1);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setRole(Role.USER);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setStartedAt(calendar.getTime());

        LeaveRequestEntity leaveRequestEntity = new LeaveRequestEntity();
        leaveRequestEntity.setDescription(description);
        leaveRequestEntity.setStartDate(dateNow);
        leaveRequestEntity.setEndDate(dateNow);
        leaveRequestEntity.setUserRequester(userEntity);
        leaveRequestEntity.setDaysOff(1);

        LeaveRequestResponse response = new LeaveRequestResponse();
        response.setId(1L);
        response.setDescription(description);
        response.setStartDate(dateNow);
        response.setEndDate(dateNow);

        when(createLeaveRequestRequestToLeaveRequestEntityConverter.convert(any())).thenReturn(leaveRequestEntity);
        when(userManagementService.getUserFromHeaderToken(anyString())).thenReturn(Optional.of(userEntity));
        when(leaveRequestRepository.findWaitingLeaveRequestEntityListByRequesterEmail(any())).thenReturn(Collections.emptyList());
        when(leaveRequestRepository.sumApprovedDaysOffSinceLastYearByRequesterId(any())).thenReturn(Optional.of(0));
        when(holidayService.getAllHolidaysAsCalendarList()).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(RequestedDaysBiggerRemainingDaysException.class).isThrownBy(() -> {
            leaveRequestService.create(request, "");
        });
    }

    private void loadMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(-1);
        messageSource.setBasenames("messages,config.i18n.messages".split(","));

        ReflectionTestUtils.setField(Translator.class, "messageSource", messageSource);
    }

}

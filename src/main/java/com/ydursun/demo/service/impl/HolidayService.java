package com.ydursun.demo.service.impl;

import com.ydursun.demo.dto.HolidayDto;
import com.ydursun.demo.model.converter.HolidayDtoToCalendarConverter;
import com.ydursun.demo.model.converter.HolidayEntityToHolidayDtoConverter;
import com.ydursun.demo.repository.HolidayRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    private final HolidayEntityToHolidayDtoConverter holidayEntityToHolidayDtoConverter;
    private final HolidayDtoToCalendarConverter holidayDtoToCalendarConverter;

    public HolidayService(HolidayRepository holidayRepository,
                          HolidayEntityToHolidayDtoConverter holidayEntityToHolidayDtoConverter,
                          HolidayDtoToCalendarConverter holidayDtoToCalendarConverter) {
        this.holidayRepository = holidayRepository;
        this.holidayEntityToHolidayDtoConverter = holidayEntityToHolidayDtoConverter;
        this.holidayDtoToCalendarConverter = holidayDtoToCalendarConverter;
    }

    public List<Calendar> getAllHolidaysAsCalendarList() {
        List<HolidayDto> holidayDtoList = getAllHolidays();

        return holidayDtoList.stream().map(holidayDtoToCalendarConverter::convert)
                .collect(Collectors.toList());
    }

    private List<HolidayDto> getAllHolidays() {
        return holidayEntityToHolidayDtoConverter.convert(holidayRepository.findAll());
    }

}

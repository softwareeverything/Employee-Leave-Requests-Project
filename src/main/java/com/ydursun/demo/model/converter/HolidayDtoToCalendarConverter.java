package com.ydursun.demo.model.converter;

import com.ydursun.demo.dto.HolidayDto;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HolidayDtoToCalendarConverter implements GenericConverter<HolidayDto, Calendar> {

    @Override
    public Calendar convert(HolidayDto source) {
        if(source == null) {
            return null;
        }

        return new GregorianCalendar(Year.now().getValue(),
                source.getMonthOfYear()-1,
                source.getDayOfMonth());
    }

}
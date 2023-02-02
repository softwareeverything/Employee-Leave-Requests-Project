package com.ydursun.demo.model.converter;

import com.ydursun.demo.dto.HolidayDto;
import com.ydursun.demo.model.entity.HolidayEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HolidayEntityToHolidayDtoConverter implements GenericConverter<HolidayEntity, HolidayDto> {

    @Override
    public HolidayDto convert(HolidayEntity source) {
        if(source == null) {
            return null;
        }

        HolidayDto target = new HolidayDto();
        target.setDayOfMonth(source.getDayOfMonth());
        target.setMonthOfYear(source.getMonthOfYear());

        return target;
    }

    public List<HolidayDto> convert(List<HolidayEntity> source) {
        return source.stream().map(this::convert).collect(Collectors.toList());
    }

}
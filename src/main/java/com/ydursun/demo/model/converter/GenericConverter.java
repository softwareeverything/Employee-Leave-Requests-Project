package com.ydursun.demo.model.converter;

public interface GenericConverter<S, T> {

    T convert(S source);

}

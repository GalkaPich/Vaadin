package com.richandhappy.myapplication.Converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class MyConverter implements Converter<Date, Long> {

	@Override
	public Result<Long> convertToModel(Date value, ValueContext context) {

		Long milliseconds = value.getTime();
		System.out.println(milliseconds);

		try {
			return Result.ok(milliseconds);
		} catch (NumberFormatException e) {
			return Result.error("Please enter a date");
		}
	}

	@Override
	public Date convertToPresentation(Long value, ValueContext context) {
		return new Date(value);
		// SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
		// String dateText = df2.format(date);
	}
}

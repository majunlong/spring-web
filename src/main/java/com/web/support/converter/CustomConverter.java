package com.web.support.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class CustomConverter  implements Converter<String, Date>{

	@Override
	public Date convert(String source) {
		return null;
	}
	
}

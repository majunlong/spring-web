package com.web.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlEnum
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "Gender", namespace = "http://model.web.com/")
public enum Gender {

	MALE("男"), FEMALE("女");

	String value;

	Gender(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static Map<String, String> getGenders() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(null, null);
		for (Gender gender : values()) {
			map.put(gender.name(), gender.value);
		}
		return map;
	}

}

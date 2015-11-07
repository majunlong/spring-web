package com.web.entity;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import com.web.model.Gender;
import com.web.model.Pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = { "portrait", "portraitMD5" })
@EqualsAndHashCode(callSuper = false, exclude = { "portrait", "portraitMD5" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "Employee", namespace = "http://model.web.com/")
public class Employee extends Pageable {

	private String id;

	@NotEmpty(message = "不能为空")
	private String name;

	@NotNull(message = "不能为空")
	private Gender gender;

	@NotNull(message = "不能为空")
	@Past(message = "不是一个过去时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	@NotEmpty(message = "不能为空")
	@Email(message = "不是一个email")
	private String email;

	@NotNull(message = "不能为空")
	@Min(message = "不能小于5000", value = 5000)
	@Max(message = "不能大于25000", value = 25000)
	@NumberFormat(pattern = "￥#,###.00")
	private Double salary;

	private Department department;

	@XmlTransient
	private MultipartFile portrait;

	@XmlTransient
	private String portraitMD5;

}

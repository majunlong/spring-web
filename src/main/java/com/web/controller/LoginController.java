package com.web.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.web.dao.EmployeeDao;
import com.web.entity.Employee;
import com.web.support.exception.NotLoginedException;

@Controller
@SessionAttributes({ "name" })
public class LoginController {

	@Autowired
	private EmployeeDao employeeDao;
	
	@RequestMapping(path = "/login/{name}", method = RequestMethod.GET)
	public String login(ModelMap model, @PathVariable String name) throws UnsupportedEncodingException {
		name = this.employeeDao.getISOEncodingRESTFulParam(name);
		Employee employee = this.employeeDao.findOneEmployeeByName(name);
		if (name.equals(employee == null ? null : employee.getName())) {
			model.put("name", name);
		} else {
			throw new NotLoginedException("该用户姓名不存在！");
		}
		return "redirect:/restful/employees/0/3";
	}

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public void login(){
		throw new NotLoginedException("登录超时，请重新登录！");
	}
	
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		model.put("name", "");
		return "/index";
	}

}

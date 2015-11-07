package com.web.webservice;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.web.entity.Employee;
import com.web.model.Result;

@Endpoint
public class EmployeeWebservice {

	@ResponsePayload
	@PayloadRoot(namespace = "http://model.web.com/", localPart = "Employee")
	public Result printEmployeeInfo(@RequestPayload Employee employee) {
		System.out.println(employee);
		return new Result(true);
	}

}

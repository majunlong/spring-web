package com.test.webservice;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringSource;

import com.web.entity.Department;
import com.web.entity.Employee;
import com.web.model.Gender;
import com.web.model.Result;

public class TestSWSWebservice {

	private Employee request;
	private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	private Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
	private String uri = "http://localhost:8080/spring-web/webservice";
	private WebServiceTemplate webServiceTemplate;

	@Before
	public void init() throws Exception {
		Employee employee = new Employee();
		employee.setId("");
		employee.setName("张三");
		employee.setGender(Gender.MALE);
		employee.setBirthday(new Date());
		employee.setEmail("zhangs@qq.com");
		employee.setSalary(5000.0);
		Department department = new Department();
		department.setId("");
		department.setName("部门A");
		employee.setDepartment(department);
		this.request = employee;
		this.marshaller.setPackagesToScan(ClassUtils.getPackageName(Employee.class));
		this.marshaller.afterPropertiesSet();
		this.unmarshaller.setPackagesToScan(ClassUtils.getPackageName(Result.class));
		this.unmarshaller.afterPropertiesSet();
		this.webServiceTemplate = new WebServiceTemplate(this.marshaller, this.unmarshaller);
		this.webServiceTemplate.setDefaultUri(this.uri);
	}

	@Test
	public void testMarshalSend() {
		Result result = (Result) this.webServiceTemplate.marshalSendAndReceive(this.request);
		System.out.println(result);
	}

	@Test
	public void testSourceSend() throws Exception {
		StringWriter requestWriter = new StringWriter();
		this.webServiceTemplate.getMarshaller().marshal(this.request, new StreamResult(requestWriter));
		Source requestSource = new StringSource(requestWriter.toString());
		StringWriter responseWriter = new StringWriter();
		this.webServiceTemplate.sendSourceAndReceiveToResult(requestSource, new StreamResult(responseWriter));
		Source responseSource = new StringSource(responseWriter.toString());
		Result result = (Result) this.webServiceTemplate.getUnmarshaller().unmarshal(responseSource);
		System.out.println(result);
	}

	@Test
	public void testSendCallback() throws Exception {
		Result result = (Result) this.webServiceTemplate.marshalSendAndReceive(this.request, new WebServiceMessageCallback() {
			public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
				System.out.println(message);
			}
		});
		System.out.println(result);
	}

}

package com.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.web.dao.EmployeeDao;
import com.web.entity.Department;
import com.web.entity.Employee;
import com.web.model.Gender;
import com.web.support.annotation.Login;

@Login
@Controller
@RequestMapping("/restful")
@SessionAttributes({ "employeePagingInfo" })
public class EmployeeController {

	@Autowired
	private EmployeeDao employeeDao;
	private Map<String, String> genders;
	private List<Department> departments;

	private final static long FILE_SIZE = 102400;
	private final static String FILE_TYPE = "image";

	@RequestMapping(path = "/employee", method = RequestMethod.GET)
	public String initSave(ModelMap model) {
		model.addAttribute("genders", genders);
		model.addAttribute("employee", new Employee());
		model.addAttribute("departments", departments);
		return "/restful/employee";
	}

	@RequestMapping(path = "/employee", method = RequestMethod.POST)
	public String save(HttpServletResponse response, ModelMap model, @Valid Employee employee, BindingResult result) {
		MultipartFile portrait = employee.getPortrait();
		if (portrait.isEmpty()) {
			result.rejectValue("portrait", "portrait", "不能为空");
		} else {
			if (!portrait.getContentType().startsWith(FILE_TYPE)) {
				result.rejectValue("portrait", "portrait", "不是一张图片");
			}
			if (portrait.getSize() > FILE_SIZE) {
				result.rejectValue("portrait", "portrait", "不能大于100KB");
			}
		}
		if ("".equals(employee.getDepartment().getId())) {
			result.rejectValue("department.id", "department.id", "不能为空");
		}
		if (this.employeeDao.validationEmployeeName(employee)) {
			result.rejectValue("name", "name", "该姓名已存在");
		}
		if (!result.hasErrors()) {
			if (this.employeeDao.saveEmployee(employee)) {
				model.addAttribute("employee", new Employee());
			} else {
				result.rejectValue("name", "name", "该姓名已存在");
			}
		}
		model.addAttribute("genders", genders);
		model.addAttribute("departments", departments);
		return "/restful/employee";
	}

	@RequestMapping(path = "/employee/{id}", method = RequestMethod.DELETE)
	public String remove(@PathVariable String id) {
		this.employeeDao.deleteEmployee(id);
		return "redirect:/restful/employees";
	}

	@ModelAttribute
	public void modifyFilter(ModelMap model, @RequestParam(name = "id", required = false) String id) {
		if (this.genders == null) {
			this.genders = Gender.getGenders();
		}
		if (this.departments == null) {
			this.departments = new ArrayList<Department>();
			this.departments.add(new Department(null, null));
			this.departments.addAll(this.employeeDao.findAllDepartment());
		}
		if (id != null) {
			Employee employee = this.employeeDao.loadEmployeeForUpdate(id);
			model.addAttribute("employee", employee);
		}
	}

	@RequestMapping(path = "/employee", method = RequestMethod.PUT)
	public String modify(ModelMap model, @Valid @ModelAttribute("employee") Employee employee, BindingResult result) {
		if ("".equals(employee.getDepartment().getId())) {
			result.rejectValue("department.id", "department.id", "不能为空");
		}
		if (this.employeeDao.validationEmployeeName(employee)) {
			result.rejectValue("name", "name", "该姓名已存在");
		}
		if (!result.hasErrors()) {
			if (this.employeeDao.saveEmployee(employee)) {
				return "redirect:/restful/employee/" + employee.getId();
			}
			result.rejectValue("name", "name", "该姓名已存在");
		}
		model.addAttribute("genders", genders);
		model.addAttribute("departments", departments);
		return "/restful/employee";
	}

	@RequestMapping(path = "/employee/{id}", method = RequestMethod.GET)
	public String one(ModelMap model, @PathVariable String id) {
		Employee employee = this.employeeDao.findOneEmployee(id);
		model.addAttribute("genders", genders);
		model.addAttribute("employee", employee);
		model.addAttribute("departments", departments);
		return "/restful/employee";
	}

	@RequestMapping(path = "/employees/{pageNumber}/{pageSize}", method = RequestMethod.GET)
	public String initlist(ModelMap model, @PathVariable int pageNumber, @PathVariable int pageSize) {
		Employee employee = new Employee();
		employee.setPageNumber(pageNumber);
		employee.setPageSize(pageSize);
		List<Employee> employees = this.employeeDao.findAllEmployee(employee);
		model.addAttribute("genders", genders);
		model.addAttribute("employee", employee);
		model.addAttribute("employees", employees);
		model.addAttribute("departments", departments);
		model.addAttribute("employeePagingInfo", employee);
		return "/restful/employees";
	}

	@RequestMapping(path = "/employees", method = RequestMethod.GET)
	public String backList(ModelMap model, @ModelAttribute("employeePagingInfo") Employee employee) {
		List<Employee> employees = this.employeeDao.findAllEmployee(employee);
		model.addAttribute("genders", genders);
		model.addAttribute("employee", employee);
		model.addAttribute("employees", employees);
		model.addAttribute("departments", departments);
		return "/restful/employees";
	}

	@RequestMapping(path = "/employees", method = RequestMethod.PATCH)
	public String list(ModelMap model, Employee employee) {
		List<Employee> employees = this.employeeDao.findAllEmployee(employee);
		model.addAttribute("genders", genders);
		model.addAttribute("employee", employee);
		model.addAttribute("employees", employees);
		model.addAttribute("departments", departments);
		model.addAttribute("employeePagingInfo", employee);
		return "/restful/employees";
	}

	@RequestMapping(path = "/employee/portrait/{id}", method = RequestMethod.GET)
	public void downLoadPortrait(HttpServletResponse response, @PathVariable String id) throws UnsupportedEncodingException {
		Employee employee = this.employeeDao.findOneEmployee(id);
		response.reset();
		response.setContentType("image/jpeg");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(employee.getName().getBytes("UTF-8"), "ISO-8859-1") + ".jpg");
		response.setCharacterEncoding("UTF-8");
		FileInputStream fis = null;
		ServletOutputStream fos = null;
		try {
			fis = new FileInputStream(this.employeeDao.getFilePath(employee));
			fos = response.getOutputStream();
			int length = -1;
			byte[] b = new byte[1024 * 5];
			while ((length = fis.read(b)) != -1) {
				fos.write(b, 0, length);
			}
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

}

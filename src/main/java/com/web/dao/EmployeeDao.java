package com.web.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Repository;

import com.web.entity.Department;
import com.web.entity.Employee;

@Repository
public class EmployeeDao {

	private final static LinkedList<Employee> employees = new LinkedList<Employee>();
	private final static ArrayList<Department> departments = new ArrayList<Department>();
	private final static String FILE_PATH = "C:\\Users\\Administrator\\Desktop\\MyPortrait";

	static {
		departments.add(new Department(UUID.randomUUID().toString(), "部门A"));
		departments.add(new Department(UUID.randomUUID().toString(), "部门B"));
		departments.add(new Department(UUID.randomUUID().toString(), "部门C"));
		for (int i = 0; i < 100; i++) {
			String[] name = EmployeeObjectFactory.getName();
			byte[] portrait = EmployeeObjectFactory.getPortrait();
			Employee employee = new Employee();
			employee.setId(UUID.randomUUID().toString());
			employee.setName(name[0]);
			employee.setGender(EmployeeObjectFactory.getGender());
			employee.setBirthday(EmployeeObjectFactory.getBirthday());
			employee.setEmail(name[1] + EmployeeObjectFactory.getEmail());
			employee.setSalary(EmployeeObjectFactory.getSalary());
			employee.setDepartment(EmployeeObjectFactory.getDepartment(departments));
			employee.setPortraitMD5(DigestUtils.md5Hex(portrait).toUpperCase());
			employees.add(employee);
			File path = new File(FILE_PATH);
			if (!path.exists()) {
				path.mkdir();
			}
			File file = new File(FILE_PATH + "\\MD5." + employee.getPortraitMD5() + ".jpg");
			if (!file.exists()) {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					fos.write(portrait);
					fos.flush();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
						}
					}
				}

			}
		}
	}

	public String getFilePath(Employee employee) {
		return FILE_PATH + "\\MD5." + employee.getPortraitMD5() + ".jpg";
	}

	public String getISOEncodingRESTFulParam(String param) throws UnsupportedEncodingException {
		if (param.equals(new String(param.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
			return new String(param.getBytes("ISO-8859-1"), "UTF-8");
		}
		return param;
	}

	public synchronized boolean validationEmployeeName(Employee employee) {
		for (Employee emp : employees) {
			if (emp.getName().equals(employee.getName()) && !emp.getId().equals(employee.getId())) {
				return true;
			}
		}
		return false;
	}

	private void savePortrait(Employee employee) {
		OutputStream fos = null;
		try {
			byte[] bytes = employee.getPortrait().getBytes();
			String portraitMd5 = DigestUtils.md5Hex(bytes).toUpperCase();
			employee.setPortraitMD5(portraitMd5);
			File file = new File(FILE_PATH + "\\MD5." + portraitMd5 + ".jpg");
			if (file.exists()) {
				return;
			}
			fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			employee.setPortrait(null);
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public synchronized boolean saveEmployee(Employee employee) {
		if (this.validationEmployeeName(employee)) {
			return false;
		}
		if (employee.getId() == null) {
			employee.setId(UUID.randomUUID().toString());
			employee.setDepartment(this.findOneDepartment(employee.getDepartment().getId()));
			this.savePortrait(employee);
			employees.add(employee);
		} else {
			Employee emp = this.findOneEmployee(employee.getId());
			emp.setName(employee.getName());
			emp.setGender(employee.getGender());
			emp.setEmail(employee.getEmail());
			emp.setSalary(employee.getSalary());
			emp.setDepartment(this.findOneDepartment(employee.getDepartment().getId()));
		}
		return true;
	}

	public void deleteEmployee(String id) {
		for (int i = 0; i < employees.size(); i++) {
			if (id.equals(employees.get(i).getId())) {
				employees.remove(i);
				return;
			}
		}
	}

	public Employee findOneEmployee(String id) {
		for (Employee employee : employees) {
			if (id.equals(employee.getId())) {
				return employee;
			}
		}
		return null;
	}

	public Employee findOneEmployeeByName(String name) {
		for (Employee employee : employees) {
			if (name.equals(employee.getName())) {
				return employee;
			}
		}
		return null;
	}

	public Employee loadEmployeeForUpdate(String id) {
		Employee employee = new Employee();
		Employee emp = this.findOneEmployee(id);
		employee.setId(emp.getId());
		employee.setName(emp.getName());
		employee.setGender(emp.getGender());
		employee.setBirthday(emp.getBirthday());
		employee.setEmail(emp.getEmail());
		employee.setSalary(emp.getSalary());
		Department department = new Department();
		Department depart = emp.getDepartment();
		department.setId(depart.getId());
		department.setName(depart.getName());
		employee.setDepartment(department);
		return employee;
	}

	public List<Employee> findAllEmployee(Employee employee) {
		List<Employee> cache = new LinkedList<Employee>();
		List<Employee> result = new LinkedList<Employee>(employees);
		if (employee.getName() != null && !"".equals(employee.getName())) {
			for (Employee emp : result) {
				if (emp.getName().indexOf(employee.getName()) >= 0) {
					cache.add(emp);
				}
			}
			result.clear();
			result.addAll(cache);
			cache.clear();
		}
		if (employee.getGender() != null) {
			for (Employee emp : result) {
				if (emp.getGender() == employee.getGender()) {
					cache.add(emp);
				}
			}
			result.clear();
			result.addAll(cache);
			cache.clear();
		}
		if (employee.getDepartment() != null && employee.getDepartment().getId() != null && !"".equals(employee.getDepartment().getId())) {
			for (Employee emp : result) {
				if (emp.getDepartment().getId().equals(employee.getDepartment().getId())) {
					cache.add(emp);
				}
			}
			result.clear();
			result.addAll(cache);
			cache.clear();
		}
		employee.setRows(result.size());
		if (result.size() == 0) {
			return null;
		}
		int begin = (employee.getPageNumber() - 1) * employee.getPageSize();
		for (int i = 0; i < result.size(); i++) {
			if (i >= begin && i < begin + employee.getPageSize()) {
				cache.add(result.get(i));
			}
		}
		result.clear();
		result.addAll(cache);
		cache.clear();
		return result;
	}

	public List<Employee> findAllEmployee() {
		return employees;
	}

	public Department findOneDepartment(String id) {
		for (Department department : departments) {
			if (id.equals(department.getId())) {
				return department;
			}
		}
		return null;
	}

	public List<Department> findAllDepartment() {
		return departments;
	}

}

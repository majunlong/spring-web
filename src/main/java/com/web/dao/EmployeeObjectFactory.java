package com.web.dao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.web.entity.Department;
import com.web.model.Gender;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EmployeeObjectFactory {

	private static int[] count = new int[4];
	private static ArrayList<byte[]> portraits = null;

	static {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(EmployeeObjectFactory.class.getClassLoader().getResourceAsStream("repository/portrait.serializable"));
			portraits = (ArrayList) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String[] getName() {
		Random random = new Random();
		switch (random.nextInt(4)) {
		case 0:
			count[0]++;
			return new String[] { "张三" + count[0], "zhangsan" + count[0] };
		case 1:
			count[1]++;
			return new String[] { "李四" + count[1], "lisi" + count[1] };
		case 2:
			count[2]++;
			return new String[] { "王五" + count[2], "wangwu" + count[2] };
		case 3:
			count[3]++;
			return new String[] { "赵六" + count[3], "zhaoliu" + count[3] };
		}
		return null;
	}

	public static Gender getGender() {
		Random random = new Random();
		switch (random.nextInt(2)) {
		case 0:
			return Gender.MALE;
		case 1:
			return Gender.FEMALE;
		}
		return null;
	}

	public static Date getBirthday() {
		return new Date(Long.parseLong(new Random().nextInt(315532800) + "") * 1000 + 315504000000L);
	}

	public static String getEmail() {
		Random random = new Random();
		switch (random.nextInt(4)) {
		case 0:
			return "@qq.com";
		case 1:
			return "@163.com";
		case 2:
			return "@sina.com";
		case 3:
			return "@sohu.com";
		}
		return null;
	}

	public static Double getSalary() {
		return new Random().nextDouble() * 20000 + 5000;
	}

	public static Department getDepartment(List<Department> departments) {
		return departments.get(new Random().nextInt(departments.size()));
	}

	public static byte[] getPortrait() {
		return portraits.get(new Random().nextInt(portraits.size()));
	}

}

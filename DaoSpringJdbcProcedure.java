package com.spring.daoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.spring.dao.EmployeeDao;
import com.spring.model.Employee;

@Repository("employeeDao")
public class EmployeeImpl implements EmployeeDao {

	@Autowired
	SimpleJdbcCall simplejdbc;


	/*
	 * DELIMITER $$ CREATE PROCEDURE addEmployee() BEGIN INSERT INTO
	 * `employee`( `name`, `address`, `email`) VALUES (in_name,in_address,in_email);
	 * 
	 * SELECT max(empid) INTO out_emp FROM employee;
	 * 
	 * end
	 * 
	 */
	@Override
	public int addEmployeeDetails(Employee emp) {

		Map<String, Object> parameters = new HashMap<String, Object>(3);
		// parameters.put("id", emp.getEmpid());
		parameters.put("in_name", emp.getName());
		parameters.put("in_address", emp.getAddress());
		parameters.put("in_email", emp.getEmail());
		// TODO Auto-generated method stub
		simplejdbc.withProcedureName("addEmployee");
		Map<String, Object> mapinput = simplejdbc.execute(parameters);
		int id = (Integer) mapinput.get("out_emp");
		return id;
	}
	/*
	 * we call the returningResultSet() method with two arguments: the first one is
	 * for the name of a key in the returning Map which we will use to read the
	 * result. And the second one is a RowMapper to read the result set directly.
	 * 
	 * @Override public List<Employee> getAllDetails() {
	 * simplejdbc.withProcedureName("getAllEmployeeDetails").returningResultSet(
	 * "employees", new EmployeeRowMapper()); Map<String, Object> mapinput
	 * =simplejdbc.execute(); List<Employee> emplist
	 * =(List<Employee>)mapinput.get("employees"); return emplist; }
	 */
	// 2 way
	/*
	 * You can used BeanPropertyRowMapper agar tumhare bean k column exactly match
	 * hai tumhare database k column se toh no need to write RowMapper
	 * 
	 * @Override public List<Employee> getAllDetails() {
	 * simplejdbc.withProcedureName("getAllEmployeeDetails").returningResultSet(
	 * "employees", BeanPropertyRowMapper.newInstance(Employee.class)); Map<String,
	 * Object> mapinput =simplejdbc.execute(); List<Employee> emplist
	 * =(List<Employee>)mapinput.get("employees"); return emplist; }
	 */

	/*
	 * if you want to return some List of partial value then used this its too
	 * complex but
	 */

	@Override
	public List<Employee> getAllDetails() {
		List<Employee> emplist = new ArrayList<Employee>();

		simplejdbc.withProcedureName("getAllEmployeeDetails");
		Map<String, Object> mapinput = simplejdbc.execute();
		List object = (List) mapinput.get("#result-set-1");
		for (Object object2 : object) {
			Employee emp = new Employee();
			Map<String, Object> emp1 = (Map<String, Object>) object2;
			emp.setName((String) emp1.get("name"));
			emp.setEmpid(Long.valueOf(emp1.get("empid").toString()));
			emp.setEmail((String) emp1.get("email"));
			emplist.add(emp);
		}
		return emplist;
	}

	@Override
	public Employee getEmployeeById(long empid) {
		Employee emp = null;
		try {
			simplejdbc.withProcedureName("getEmployeeById");
			Map<String, Object> in = new HashMap<String, Object>();
			in.put("in_empid", empid);

			Map<String, Object> map1 = simplejdbc.execute(in);
			List object = (List) map1.get("#result-set-1");
			Map<String, Object> map = (Map<String, Object>) object.get(0);

			emp = new Employee();
			emp.setEmpid(Long.valueOf(map.get("empid") + ""));
			emp.setName((String) map.get("name"));
			emp.setEmail((String) map.get("email"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return emp;
	}

	@Override
	public Employee updateEmployee(Employee emp) {
		// TODO Auto-generated method st ub
		return null;
	}

	@Override
	public int deleteEmployee(long enmpid) {
		// TODO Auto-generated method stub
		return 0;
	}

}

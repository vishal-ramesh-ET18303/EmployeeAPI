package com.vishalramesh.employee.dao;

import com.vishalramesh.employee.model.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}

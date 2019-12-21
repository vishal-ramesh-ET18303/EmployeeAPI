package com.vishalramesh.employee.comtroller;

import com.vishalramesh.employee.controller.EmployeeController;
import com.vishalramesh.employee.dao.EmployeeRepository;
import com.vishalramesh.employee.model.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private Employee employee;

    @InjectMocks
    private EmployeeController employeeController;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Before
    public void setupMocks(){
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    @DisplayName("Testing Adding an Employee")
    public void addEmployeeTest(){
        Map testEmployeeData = new HashMap<String, String>();
        testEmployeeData.put("name", employee.getName());
        testEmployeeData.put("email", employee.getEmail());
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
        ResponseEntity response = employeeController.addEmployee(testEmployeeData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Testing Finding and Employee")
    public void findEmployeeTest(){
        Mockito.when(employeeRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        ResponseEntity response = employeeController.findEmployee(employee.getEmpId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.when(employeeRepository.existsById(Mockito.anyInt())).thenReturn(false);
        response = employeeController.findEmployee(employee.getEmpId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Testing Updating an employee")
    public void updateEmployeeTest(){
        Map testEmployeeData = new HashMap<String, String>();
        testEmployeeData.put("empId", employee.getEmpId());
        testEmployeeData.put("name", employee.getName());
        testEmployeeData.put("email", employee.getEmail());
        Mockito.when(employeeRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
        ResponseEntity response = employeeController.updateEmployee(testEmployeeData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Tesing Deleting and Employee")
    public void deleteEmployeeTest(){
        Mockito.when(employeeRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        Mockito.doNothing().when(employeeRepository).delete(Mockito.any(Employee.class));
        ResponseEntity response = employeeController.deleteEmployee(employee.getEmpId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.when(employeeRepository.existsById(Mockito.anyInt())).thenReturn(false);
        response = employeeController.deleteEmployee(employee.getEmpId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

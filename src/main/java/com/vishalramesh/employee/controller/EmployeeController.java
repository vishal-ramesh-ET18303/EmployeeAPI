package com.vishalramesh.employee.controller;

import com.vishalramesh.employee.dao.EmployeeRepository;
import com.vishalramesh.employee.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path = "/employee")
public class EmployeeController {

    private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Employee> getAllEmployees(){
        logger.info("Getting all entries in the database.");
        return  employeeRepository.findAll();
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity addEmployee(@RequestBody Map<String, String> requestBody){
        Employee e = new Employee();
        e.setName(requestBody.get("name"));
        e.setEmail(requestBody.get("email"));
        e = employeeRepository.save(e);
        logger.info("New  user with empId {} created", e.getEmpId());
        return new ResponseEntity<Employee>(e, HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public @ResponseBody ResponseEntity findEmployee(@RequestParam Integer empId){
        logger.info("Trying to find employee with empId {}", empId);
        if (!employeeRepository.existsById(empId)){
            logger.error("Employee not found.");
            return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
        }
        logger.info("Employee exists. Retrieving Data");
        return new ResponseEntity<Employee>( employeeRepository.findById(empId).get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> updateEmployee(@RequestBody Map<String, Object> request){
        int empId = (Integer) request.get("empId");
        String email = (String) request.get("email");
        String name = (String) request.get("name");
        logger.info("Checking if employee with empId {} exists", empId);
        if(!employeeRepository.existsById(empId)){
            logger.error("Employee not found.");
            return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
        }
        logger.info("Employee exists. Retrieving Data");
        Employee e =  employeeRepository.findById(empId).get();
        if(email != null)
            e.setEmail(email);
        if(name != null)
            e.setName(name);
        logger.info("Updating employee {}", e.getEmpId());
        e = employeeRepository.save(e);
        return new ResponseEntity<Employee>(e, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<?> deleteEmployee(@RequestParam Integer empId){
        if(!employeeRepository.existsById(empId)){
            logger.error("Employee {} does not exist.", empId);
            return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
        }
        Employee e =  employeeRepository.findById(empId).get();
        employeeRepository.delete(e);
        logger.info("Deleted employee {}", empId);
        return new ResponseEntity<String>("[]", HttpStatus.OK);
    }
}

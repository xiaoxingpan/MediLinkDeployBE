package com.fsd08.MediLink.service;

import com.fsd08.MediLink.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

//    public List<Department> getAllDepartments() {
//        return departmentRepository.findAll();
//    }
//
//    public Department getDepartmentById(int id) {
//        return departmentRepository.findById(id).orElse(null);
//    }
//
//    public ResponseEntity<String> addDepartment(@RequestBody Department department, @RequestHeader("Authorization") String accessToken) {
//        if (department.getDepartment_name() == null || department.getDepartment_name().isEmpty()) {
//            throw new IllegalArgumentException("Department name cannot be empty");
//        }
//        if(JwtService.validateToken(accessToken)) {
//            Department addedDepartment = departmentRepository.save(department);
//            return ResponseEntity.ok("Department added successfully");
//        }
//        else {
//            // Token is invalid, return unauthorized response
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
//        }
//    }
//
//    public Department updateDepartment(Department department) {
//        if (department.getDepartment_name() == null || department.getDepartment_name().isEmpty()) {
//            throw new IllegalArgumentException("Department name cannot be empty");
//        }
//        return departmentRepository.save(department);
//    }
//
//    public void deleteDepartment(int id) {
//        departmentRepository.deleteById(id);
//    }
//
//    public boolean departmentExists(int id) {
//        return departmentRepository.existsById(id);
//    }
}

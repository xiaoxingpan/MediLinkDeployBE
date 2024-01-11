package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.entity.Department;
import com.fsd08.MediLink.repository.DepartmentRepository;
import com.fsd08.MediLink.service.JwtService;
import com.fsd08.MediLink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
@RequiredArgsConstructor
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserService userService;




    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        logger.info("Retrieved {} departments from the database.", departments.size());
        return departments;
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<?> getOneDepartment(@PathVariable int id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department, @RequestHeader("Authorization") String accessToken) {
        if(!JwtService.validateToken(accessToken) || !isAdmin(accessToken) ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Department result =departmentRepository.save(department);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id, @RequestBody Department department, @RequestHeader("Authorization") String accessToken) {
        if (!departmentRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!JwtService.validateToken(accessToken) || !isAdmin(accessToken) ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        department.setId(id);
        Department result = departmentRepository.save(department);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Department> deleteDepartment(@PathVariable int id, @RequestHeader("Authorization") String accessToken) {
        if (!departmentRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!JwtService.validateToken(accessToken) || !isAdmin(accessToken) ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        departmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private boolean isAdmin(String accessToken) {
        String username = JwtService.extractUsernameFromToken(accessToken);
        String role = userService.findUserByUsername(username).get().getAuthority();
        return "admin".equals(role);
    }


}
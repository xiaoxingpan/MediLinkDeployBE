package com.fsd08.MediLink.repository;

import com.fsd08.MediLink.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> getDepartmentById(int id);
}

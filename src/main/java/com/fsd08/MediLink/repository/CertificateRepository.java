package com.fsd08.MediLink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fsd08.MediLink.entity.Certificate;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    List<Certificate> findByUserId(int userId);
}

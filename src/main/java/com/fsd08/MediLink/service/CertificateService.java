package com.fsd08.MediLink.service;

import com.fsd08.MediLink.repository.CertificateRepository;
import com.fsd08.MediLink.entity.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Certificate getCertificateById(int id) {
        return certificateRepository.findById(id).orElse(null);
    }

    public Certificate addCertificate(Certificate certificate) {
        System.out.println("add cert");
        return certificateRepository.save(certificate);
    }

    public Certificate updateCertificate(int id, Certificate certificate) {
        if (certificateRepository.existsById(id)) {
            certificate.setId(id);
            return certificateRepository.save(certificate);
        }
        return null;
    }

    public void deleteCertificate(int id) {
        certificateRepository.deleteById(id);
    }

    public List<Certificate> getCertificatesByUserId(int userId) {
        return certificateRepository.findByUserId(userId);
    }

}

package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.entity.Certificate;
import com.fsd08.MediLink.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public List<Certificate> getAllCertificates() {
        return certificateService.getAllCertificates();
    }


    @GetMapping("/user/{userId}")
    public List<Certificate> getCertificatesByUserId(@PathVariable int userId) {
        return certificateService.getCertificatesByUserId(userId);
    }

    @GetMapping("/{id}")
    public Certificate getCertificateById(@PathVariable int id) {
        return certificateService.getCertificateById(id);
    }

    @PostMapping
    public Certificate addCertificate(@RequestBody Certificate certificate) {
        System.out.println("certificate.user_id =======" + certificate.getUser_id());
        return certificateService.addCertificate(certificate);
    }

    @PutMapping("/{id}")
    public Certificate updateCertificate(@PathVariable int id, @RequestBody Certificate certificate) {
        return certificateService.updateCertificate(id, certificate);
    }

    @DeleteMapping("/{id}")
    public void deleteCertificate(@PathVariable int id) {
        certificateService.deleteCertificate(id);
    }

}


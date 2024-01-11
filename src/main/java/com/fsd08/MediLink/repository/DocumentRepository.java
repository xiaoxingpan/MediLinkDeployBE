package com.fsd08.MediLink.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsd08.MediLink.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

}

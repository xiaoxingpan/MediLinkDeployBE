package com.fsd08.MediLink.repository;

import com.fsd08.MediLink.entity.Authority;
import com.fsd08.MediLink.entity.AuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, AuthorityPK> {


}

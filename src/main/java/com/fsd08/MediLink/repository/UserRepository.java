package com.fsd08.MediLink.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fsd08.MediLink.dto.PatientDTO;
import com.fsd08.MediLink.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    User findById(int id);

    User findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.*,c.id AS certificateId,c.url AS certificate FROM users u LEFT JOIN certificates c ON u.id=c.user_id WHERE (:authority IS NULL OR authority LIKE concat('%',:authority,'%'))", nativeQuery = true)
    Page<PatientDTO> findPage(Pageable pageable, String authority);

}

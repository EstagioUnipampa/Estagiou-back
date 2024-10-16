package com.lab.estagiou.model.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByEmail(String email);

    @Query("SELECT u.role FROM UserEntity u WHERE u.email = :email")
    UserEntity.Role findRoleByEmail(@Param("email") String email);

}

package com.lab.estagiou.model.user;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lab.estagiou.exception.generic.RegisterException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    private Role role = Role.ROLE_STUDENT;

    public enum Role {
        ROLE_ADMIN, ROLE_STUDENT, ROLE_COMPANY
    }

    private boolean isEnabled;

    protected UserEntity(UUID id, String name, String email, String password, Role role) {

        if (name == null || name.isBlank()) {
            throw new RegisterException("Nome não pode ser nulo");
        }

        if (email == null || email.isBlank()) {
            throw new RegisterException("Email não pode ser nulo");
        }

        if (password == null || password.isBlank()) {
            throw new RegisterException("Senha não pode ser nula");
        }

        if (role == null) {
            throw new RegisterException("Role não pode ser nula");
        }

        this.id = id;
        this.email = email;
        this.name = name;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.role = role;
        this.isEnabled = false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

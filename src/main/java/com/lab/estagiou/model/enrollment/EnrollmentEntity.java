package com.lab.estagiou.model.enrollment;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lab.estagiou.dto.request.model.enrollment.EnrollmentRegisterRequest;
import com.lab.estagiou.exception.generic.RegisterException;
import com.lab.estagiou.exception.generic.UpdateException;
import com.lab.estagiou.model.admin.AdminEntity;
import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.model.student.StudentEntity;
import com.lab.estagiou.model.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "enrollment")
@Table(name = "tb_enrollment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnrollmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private byte[] file;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "jobVacancy_id")
    @JsonIgnore
    private JobVacancyEntity jobVacancy;

    public EnrollmentEntity(EnrollmentRegisterRequest request, StudentEntity student, JobVacancyEntity jobVacancy) {

        if (request == null) {
            throw new RegisterException("Request não pode ser nulo");
        }

        if (request.getStudentId() == null) {
            throw new RegisterException("Id do estudante não pode ser nulo");
        }

        if (request.getJobVacancyId() == null) {
            throw new RegisterException("Id da vaga não pode ser nulo");
        }

        // if (request.getFile() == null) {
        // throw new RegisterException("Arquivo não pode ser nulo");
        // }

        // try {
        // if (request.getFile().get(0).getBytes().length == 0) {
        // throw new RegisterException("Arquivo não pode ser vazio");
        // }
        // this.file = request.getFile().get(0).getBytes();
        // } catch (IOException e) {
        // throw new RegisterException("Erro ao tentar ler o arquivo");
        // }

        this.student = student;
        this.jobVacancy = jobVacancy;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void update(EnrollmentRegisterRequest request) {
        if (request == null) {
            throw new UpdateException("Request cannot be null");
        }

        // if (request.getFile() == null) {
        // throw new RegisterException("Arquivo não pode ser nulo");
        // }

        // try {
        // if (request.getFile().get(0).getBytes().length == 0) {
        // throw new RegisterException("Arquivo não pode ser vazio");
        // }
        // this.file = request.getFile().get(0).getBytes();
        // } catch (IOException e) {
        // throw new RegisterException("Erro ao tentar ler o arquivo");
        // }

        this.updatedAt = Instant.now();
    }

    public boolean equalStudentOrAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        UserEntity user = (UserEntity) authentication.getPrincipal();

        return equalStudent(user) || user instanceof AdminEntity;
    }

    public boolean equalStudent(UserEntity user) {
        if (user == null) {
            return false;
        }

        if (!(user instanceof StudentEntity)) {
            return false;
        }

        return this.student.equals(user);
    }

}

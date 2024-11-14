package com.lab.estagiou.model.enrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.model.student.StudentEntity;

public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, UUID> {

    @Query("SELECT EXISTS(SELECT 1 FROM enrollment e WHERE e.student = :studentEntity AND e.jobVacancy = :jobVacancyEntity)")
    public boolean existsByStudentIdAndJobVacancyId(StudentEntity studentEntity, JobVacancyEntity jobVacancyEntity);

    public List<EnrollmentEntity> findByStudentId(UUID id);

    public boolean existsByStudentIdAndJobVacancyId(UUID id, UUID id2);

    public Optional<EnrollmentEntity> findByStudentAndJobVacancy(StudentEntity student, JobVacancyEntity jobVacancy);
}

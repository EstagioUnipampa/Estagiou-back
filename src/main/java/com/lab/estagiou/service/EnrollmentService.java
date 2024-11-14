package com.lab.estagiou.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.lab.estagiou.dto.request.model.enrollment.EnrollmentRegisterRequest;
import com.lab.estagiou.exception.generic.NoContentException;
import com.lab.estagiou.exception.generic.NotFoundException;
import com.lab.estagiou.exception.generic.RegisterException;
import com.lab.estagiou.jwt.JwtUserDetails;
import com.lab.estagiou.model.enrollment.EnrollmentEntity;
import com.lab.estagiou.model.enrollment.EnrollmentRepository;
import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.model.jobvacancy.JobVacancyRepository;
import com.lab.estagiou.model.student.StudentEntity;
import com.lab.estagiou.model.student.StudentRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JobVacancyRepository jobVacancyRepository;

    private static final String ENROLLMENT_NOT_FOUND = "Enrollment not found: ";

    public EnrollmentEntity registerEnrollment(EnrollmentRegisterRequest request, JwtUserDetails userDetails) {
        StudentEntity student = studentRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        JobVacancyEntity jobVacancy = jobVacancyRepository.findById(request.getJobVacancyId())
                .orElseThrow(() -> new NotFoundException("Job vacancy not found"));

        if (enrollmentRepository.existsByStudentIdAndJobVacancyId(student, jobVacancy)) {
            throw new RegisterException("Inscrição já realizada");
        }

        EnrollmentEntity enrollment = new EnrollmentEntity(request, student, jobVacancy);

        return enrollmentRepository.save(enrollment);
    }

    public List<EnrollmentEntity> listEnrollments() {
        List<EnrollmentEntity> enrollments = enrollmentRepository.findAll();

        if (enrollments.isEmpty()) {
            throw new NoContentException("No enrollments registered");
        }

        return enrollments;
    }

    public ResponseEntity<Object> searchEnrollmentById(UUID id) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));

        return ResponseEntity.ok(enrollment);
    }

    public ResponseEntity<Object> searchEnrollmentFileById(UUID id) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // headers.setContentLength(enrollment.getFile().length);

        // return ResponseEntity.ok().headers(headers).body(enrollment.getFile());

        return null;
    }

    public ResponseEntity<Object> deleteEnrollmentById(UUID id, Authentication authentication) {
        // if (authentication == null) {
        // throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT);
        // }

        // EnrollmentEntity enrollment = enrollmentRepository.findById(id)
        // .orElseThrow(() -> new NotFoundException(ENROLLMENT_NOT_FOUND + id));

        // if (!enrollment.equalStudentOrAdmin(authentication)) {
        // throw new UnauthorizedUserException(
        // UNAUTHORIZED_ACESS_ATTEMPT_DOTS + ((UserEntity)
        // authentication.getPrincipal()).getId());
        // }

        // enrollmentRepository.deleteById(id);

        // log(LogEnum.INFO, "Enrollment deleted: " + id,
        // HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Object> updateEnrollment(UUID id, EnrollmentRegisterRequest request,
            Authentication authentication) {
        // if (authentication == null) {
        // throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT);
        // }

        EnrollmentEntity enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ENROLLMENT_NOT_FOUND + id));

        // if (!enrollment.equalStudentOrAdmin(authentication)) {
        // throw new UnauthorizedUserException(
        // UNAUTHORIZED_ACESS_ATTEMPT_DOTS + ((UserEntity)
        // authentication.getPrincipal()).getId());
        // }

        enrollment.update(request);
        enrollmentRepository.save(enrollment);

        // log(LogEnum.INFO, "Enrollment updated: " + enrollment.getId(),
        // HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

    public List<EnrollmentEntity> searchStudentsEnrollmentsById(UUID id) {
        return enrollmentRepository.findByStudentId(id);
    }

    public boolean isEnroll(UUID id, UUID id2) {
        return enrollmentRepository.existsByStudentIdAndJobVacancyId(id, id2);
    }

}

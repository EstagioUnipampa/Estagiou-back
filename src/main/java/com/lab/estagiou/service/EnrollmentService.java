package com.lab.estagiou.service;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lab.estagiou.dto.request.model.enrollment.EnrollmentRegisterRequest;
import com.lab.estagiou.exception.generic.NotFoundException;
import com.lab.estagiou.exception.generic.RegisterException;
import com.lab.estagiou.model.enrollment.EnrollmentEntity;
import com.lab.estagiou.model.enrollment.EnrollmentRepository;
import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.model.jobvacancy.JobVacancyRepository;
import com.lab.estagiou.model.student.StudentEntity;
import com.lab.estagiou.model.student.StudentRepository;
import com.lab.estagiou.service.util.UtilService;

@Service
public class EnrollmentService extends UtilService {
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JobVacancyRepository jobVacancyRepository;

    public ResponseEntity<Object> registerEnrollment(EnrollmentRegisterRequest request) {
        StudentEntity student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        JobVacancyEntity jobVacancy = jobVacancyRepository.findById(request.getJobVacancyId())
                .orElseThrow(() -> new NotFoundException("Job vacancy not found"));

        if (enrollmentRepository.existsByStudentIdAndJobVacancyId(student, jobVacancy)) {
            throw new RegisterException("Inscrição já realizada");
        }

        if (request.getFile() == null) {
            throw new RegisterException("Um arquivo é necessário");
        }

        String contentType = request.getFile().getContentType();
        if (contentType == null) {
            throw new RegisterException("Formato de arquivo inválido");
        }

        if (!contentType.equals(MediaType.APPLICATION_PDF_VALUE)) {
            throw new RegisterException("Formato de arquivo inválido");
        }

        EnrollmentEntity enrollment = new EnrollmentEntity(request, student, jobVacancy);
        enrollmentRepository.save(enrollment);

        URI uri = URI.create("/enrollment/" + enrollment.getId());

        return ResponseEntity.created(uri).build();
    }

    public ResponseEntity<Object> getEnrollment(UUID id) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));

        return ResponseEntity.ok(enrollment);
    }

}

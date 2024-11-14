package com.lab.estagiou.dto.response.enrollment;

import java.util.List;

import com.lab.estagiou.model.enrollment.EnrollmentEntity;
import com.lab.estagiou.model.enrollment.EnrollmentEntity.Status;

import lombok.Data;

@Data
public class EnrollmentResponse {

    private StudentResponse student;
    private JobVacancyResponse jobVacancy;
    private String status;
    private boolean isEnroll;

    public EnrollmentResponse(EnrollmentEntity enrollmentEntity) {
        this.student = new StudentResponse(enrollmentEntity.getStudent());
        this.jobVacancy = new JobVacancyResponse(enrollmentEntity.getJobVacancy());
        this.isEnroll = false;

        Status status = enrollmentEntity.getStatus();

        if (status == Status.PENDENTE) {
            this.status = "Pendente";
        } else if (status == Status.APROVADO) {
            this.status = "Aprovado";
        } else if (status == Status.REJEITADO) {
            this.status = "Rejeitado";
        }
    }

    public EnrollmentResponse(EnrollmentEntity enrollmentEntity, boolean isEnroll) {
        this.student = new StudentResponse(enrollmentEntity.getStudent());
        this.jobVacancy = new JobVacancyResponse(enrollmentEntity.getJobVacancy());
        this.isEnroll = isEnroll;

        Status status = enrollmentEntity.getStatus();

        if (status == Status.PENDENTE) {
            this.status = "Pendente";
        } else if (status == Status.APROVADO) {
            this.status = "Aprovado";
        } else if (status == Status.REJEITADO) {
            this.status = "Rejeitado";
        }
    }

    public static List<EnrollmentResponse> convertList(List<EnrollmentEntity> enrollments) {
        return enrollments.stream().map(EnrollmentResponse::new).toList();
    }

    public static List<EnrollmentResponse> convertList(List<EnrollmentEntity> enrollments, boolean isEnroll) {
        return enrollments.stream().map(enrollment -> new EnrollmentResponse(enrollment, isEnroll)).toList();
    }

}

package com.lab.estagiou.dto.response.enrollment;

import java.util.List;

import com.lab.estagiou.model.enrollment.EnrollmentEntity;

import lombok.Data;

@Data
public class EnrollmentResponse {

    private StudentResponse student;
    private JobVacancyResponse jobVacancy;

    public EnrollmentResponse(EnrollmentEntity enrollmentEntity) {
        this.student = new StudentResponse(enrollmentEntity.getStudent());
        this.jobVacancy = new JobVacancyResponse(enrollmentEntity.getJobVacancy());
    }

    public static List<EnrollmentResponse> convertList(List<EnrollmentEntity> enrollments) {
        return enrollments.stream().map(EnrollmentResponse::new).toList();
    }

}

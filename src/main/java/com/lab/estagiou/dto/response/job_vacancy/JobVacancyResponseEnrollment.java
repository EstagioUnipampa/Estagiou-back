package com.lab.estagiou.dto.response.job_vacancy;

import java.util.List;
import java.util.stream.Collectors;

import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JobVacancyResponseEnrollment extends JobVacancyResponse {

    private List<StudentResponse> enrollments;

    public JobVacancyResponseEnrollment(JobVacancyEntity entity) {
        super(entity);
        this.enrollments = StudentResponse.toDto(entity.getEnrollments().stream()
                .map(enrollment -> enrollment.getStudent()).collect(Collectors.toList()));
    }

}

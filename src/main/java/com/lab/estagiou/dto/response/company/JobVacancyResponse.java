package com.lab.estagiou.dto.response.company;

import java.util.UUID;

import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;

import lombok.Data;

@Data
public class JobVacancyResponse {

    private UUID id;
    private String title;
    private String role;
    private String description;
    private String salary;
    private String hours;
    private String modality;

    public JobVacancyResponse(JobVacancyEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.role = entity.getRole();
        this.description = entity.getDescription();
        this.salary = entity.getSalary();
        this.hours = entity.getHours();
        this.modality = entity.getModality();
    }

}

package com.lab.estagiou.dto.response.job_vacancy;

import java.util.List;

import java.util.UUID;

import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Collectors;

@Data
public class JobVacancyResponse {

    private UUID id;
    private String title;
    private String role;
    private String description;
    private String salary;
    private String hours;
    private String modality;
    private CompanyResponse company;
    private List<SkillResponse> skills;

    public JobVacancyResponse(JobVacancyEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.role = entity.getRole();
        this.description = entity.getDescription();
        this.salary = entity.getSalary();
        this.hours = entity.getHours();
        this.modality = entity.getModality();
        this.company = new CompanyResponse(entity.getCompany());
        this.skills = entity.getSkills().stream().map(SkillResponse::new).collect(Collectors.toList());
    }

    public static List<JobVacancyResponse> fromEntityList(List<JobVacancyEntity> jobVacancies) {
        return jobVacancies.stream().map(JobVacancyResponse::new).collect(Collectors.toList());
    }

}

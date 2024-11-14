package com.lab.estagiou.dto.request.model.jobvacancy;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class JobVacancyRegisterRequest {

    private String title;
    private String role;
    private String description;
    private String salary;
    private String hours;
    private String modality;
    private List<UUID> skills;
    private UUID course;

}

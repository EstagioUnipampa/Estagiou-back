package com.lab.estagiou.dto.request.model.enrollment;

import java.util.UUID;

import lombok.Data;

@Data
public class EnrollmentRegisterRequest {

    private UUID jobVacancyId;
    private UUID studentId;

}

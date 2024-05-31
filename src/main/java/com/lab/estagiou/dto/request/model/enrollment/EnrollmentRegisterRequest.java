package com.lab.estagiou.dto.request.model.enrollment;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EnrollmentRegisterRequest {

    private UUID studentId;
    private UUID jobVacancyId;

    private List<MultipartFile> file;
    
}

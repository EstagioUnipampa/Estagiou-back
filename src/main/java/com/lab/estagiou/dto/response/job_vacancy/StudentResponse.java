package com.lab.estagiou.dto.response.job_vacancy;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lab.estagiou.model.skill.SkillEntity;
import com.lab.estagiou.model.student.StudentEntity;

import lombok.Data;

@Data
public class StudentResponse {

    private UUID id;
    private String email;
    private String name;
    private String lastName;
    private String courseName;

    @JsonInclude(Include.NON_NULL)
    private List<String> skills;

    public StudentResponse(StudentEntity studentEntity) {
        this.id = studentEntity.getId();
        this.email = studentEntity.getEmail();
        this.name = studentEntity.getName();
        this.lastName = studentEntity.getLastName();
        this.courseName = studentEntity.getCourse().getName();
        if (studentEntity.getSkills() != null && !studentEntity.getSkills().isEmpty()) {
            this.skills = studentEntity.getSkills().stream().map(SkillEntity::getName).collect(Collectors.toList());
        }
    }

    public static List<StudentResponse> toDto(List<StudentEntity> students) {
        return students.stream().map(StudentResponse::new).collect(Collectors.toList());
    }

}

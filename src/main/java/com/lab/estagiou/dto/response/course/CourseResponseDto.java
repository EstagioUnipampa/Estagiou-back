package com.lab.estagiou.dto.response.course;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lab.estagiou.model.course.CourseEntity;

import lombok.Data;

@Data
public class CourseResponseDto {

    private UUID id;
    private String name;
    private List<SkillResponseDto> skills;

    public CourseResponseDto(CourseEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.skills = SkillResponseDto.toDto(entity.getSkills());
    }

    public static List<CourseResponseDto> toDto(List<CourseEntity> courses) {
        return courses.stream()
                .map(CourseResponseDto::new)
                .collect(Collectors.toList());
    }

}

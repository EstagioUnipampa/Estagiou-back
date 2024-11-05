package com.lab.estagiou.dto.response.course;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lab.estagiou.model.skill.SkillEntity;

import lombok.Data;

@Data
public class SkillResponseDto {

    private UUID id;
    private String name;

    public SkillResponseDto(SkillEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public static List<SkillResponseDto> toDto(List<SkillEntity> skills) {
        return skills.stream()
                .map(SkillResponseDto::new)
                .collect(Collectors.toList());
    }

}

package com.lab.estagiou.dto.response.job_vacancy;

import java.util.UUID;

import com.lab.estagiou.model.skill.SkillEntity;

import lombok.Data;

@Data
public class SkillResponse {

    private UUID id;
    private String name;

    public SkillResponse(SkillEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

}

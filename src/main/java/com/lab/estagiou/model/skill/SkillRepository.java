package com.lab.estagiou.model.skill;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<SkillEntity, UUID> {

}

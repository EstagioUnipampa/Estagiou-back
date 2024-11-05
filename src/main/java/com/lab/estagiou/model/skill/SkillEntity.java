package com.lab.estagiou.model.skill;

import java.io.Serializable;
import java.util.UUID;

import com.lab.estagiou.model.course.CourseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "skill")
@Table(name = "tb_skill")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SkillEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true)
    private CourseEntity course;

    public SkillEntity(String name) {
        this.name = name;
    }

}

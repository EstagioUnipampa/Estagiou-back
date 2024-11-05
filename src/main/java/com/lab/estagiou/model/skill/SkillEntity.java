package com.lab.estagiou.model.skill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lab.estagiou.model.course.CourseEntity;
import com.lab.estagiou.model.student.StudentEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    @JoinTable(name = "tb_student_skill", joinColumns = @JoinColumn(name = "skill_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<StudentEntity> students;

    public SkillEntity(String name) {
        this.name = name;
    }

    public SkillEntity addStudent(StudentEntity entity) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(entity);
        return this;
    }

}

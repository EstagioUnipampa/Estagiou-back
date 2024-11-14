package com.lab.estagiou.model.course;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.model.skill.SkillEntity;
import com.lab.estagiou.model.student.StudentEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "course")
@Table(name = "tb_course")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<StudentEntity> students;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<SkillEntity> skills;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<JobVacancyEntity> jobVacancies;

    private static final String STUDENT_NULL = "Aluno não pode ser nulo";

    public CourseEntity(String name, List<SkillEntity> skills) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do curso não pode ser nulo");
        }

        this.id = null;
        this.name = name;
        this.students = new ArrayList<>();
        this.skills = skills;
    }

    public boolean equalsName(String name) {
        return this.name.equals(name);
    }

    public int getQuantityStudents() {
        return this.students.size();
    }

    public boolean addStudent(StudentEntity student) {
        if (student == null) {
            throw new IllegalArgumentException(STUDENT_NULL);
        }

        return this.students.add(student);
    }

    public boolean removeStudent(StudentEntity student) {
        if (student == null) {
            throw new IllegalArgumentException(STUDENT_NULL);
        }

        return this.students.remove(student);
    }

    public boolean containsStudent(StudentEntity student) {
        if (student == null) {
            throw new IllegalArgumentException(STUDENT_NULL);
        }

        return this.students.contains(student);
    }

    public boolean isEmpty() {
        return this.students.isEmpty();
    }

}

package com.lab.estagiou.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lab.estagiou.model.student.StudentEntity;

class StudentTests {

    private static final String EXPECT_DOMAIN = StudentEntity.getExpectedDomain();

    private static final String VALID_EMAIL = "test" + EXPECT_DOMAIN;

    private static final String VALID_PASSWORD = "123456";

    @Test
    @DisplayName("Test create student")
    void testCreateStudent() {
        StudentEntity student = new StudentEntity("João", "Silva", VALID_EMAIL, VALID_PASSWORD);
        assertNotNull(student);
    }

    @Test
    @DisplayName("Test create student with null name")
    void testCreateStudentWithoutName() {
        try {
            new StudentEntity(null, "Silva", VALID_EMAIL, VALID_PASSWORD);
        } catch (IllegalArgumentException e) {
            assertEquals("Nome não pode ser nulo", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test create student with empty name")
    void testCreateStudentWithEmptyName() {
        try {
            new StudentEntity("", "Silva", VALID_EMAIL, VALID_PASSWORD);
        } catch (IllegalArgumentException e) {
            assertEquals("Nome não pode ser nulo", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test create student with null last name")
    void testCreateStudentWithoutLastName() {
        try {
            new StudentEntity("João", null, VALID_EMAIL, VALID_PASSWORD);
        } catch (IllegalArgumentException e) {
            assertEquals("Sobrenome não pode ser nulo", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test create student with empty last name")
    void testCreateStudentWithEmptyLastName() {
        try {
            new StudentEntity("João", "", VALID_EMAIL, VALID_PASSWORD);
        } catch (IllegalArgumentException e) {
            assertEquals("Sobrenome não pode ser nulo", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test create student with null email")
    void testCreateStudentWithoutEmail() {
        try {
            new StudentEntity("João", "Silva", null, VALID_PASSWORD);
        } catch (IllegalArgumentException e) {
            assertEquals("Email não pode ser nulo", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test create student with empty email")
    void testCreateStudentWithEmptyEmail() {
        try {
            new StudentEntity("João", "Silva", "", VALID_PASSWORD);
        } catch (IllegalArgumentException e) {
            assertEquals("Email não pode ser nulo", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test add null enrollment")
    void testAddNullInscricao() {
        StudentEntity student = new StudentEntity("João", "Silva", VALID_EMAIL, VALID_PASSWORD);
        try {
            student.addEnrollment(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Inscrição não pode ser nula", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test remove null enrollment")
    void testRemoveNullInscricao() {
        StudentEntity student = new StudentEntity("João", "Silva", VALID_EMAIL, VALID_PASSWORD);
        try {
            student.removeEnrollment(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Inscrição não pode ser nula", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test contains null enrollment")
    void testContainsNullInscricao() {
        StudentEntity student = new StudentEntity("João", "Silva", VALID_EMAIL, VALID_PASSWORD);
        try {
            student.containsEnrollment(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Inscrição não pode ser nula", e.getMessage());
        }
    }

}

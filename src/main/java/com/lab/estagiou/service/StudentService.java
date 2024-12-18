package com.lab.estagiou.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import com.lab.estagiou.dto.request.model.student.StudentRegisterRequest;
import com.lab.estagiou.exception.generic.EmailAlreadyRegisteredException;
import com.lab.estagiou.exception.generic.NoContentException;
import com.lab.estagiou.exception.generic.NotFoundException;
import com.lab.estagiou.model.course.CourseEntity;
import com.lab.estagiou.model.course.CourseRepository;
import com.lab.estagiou.model.emailconfirmationtoken.EmailConfirmationTokenEntity;
import com.lab.estagiou.model.emailconfirmationtoken.EmailConfirmationTokenRepository;
import com.lab.estagiou.model.log.LogEnum;
import com.lab.estagiou.model.skill.SkillEntity;
import com.lab.estagiou.model.skill.SkillRepository;
import com.lab.estagiou.model.student.StudentEntity;
import com.lab.estagiou.model.student.StudentRepository;
import com.lab.estagiou.model.user.UserEntity;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailSendService emailService;

    @Autowired
    private EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    @Value("${spring.mail.enable}")
    private boolean mailInviteEnabled;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SkillRepository skillRepository;

    private static final String STUDENT_NOT_FOUND = "Student not found: ";

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);

    public ResponseEntity<Object> registerStudent(StudentEntity student, StudentRegisterRequest request) {

        CourseEntity courseEntity = courseRepository.findById(request.getCourse())
                .orElseThrow(() -> new EntityNotFoundException("Curso inexistente"));

        student.setCourse(courseEntity);

        if (request.getSkills() != null || !request.getSkills().isEmpty()) {
            List<UUID> skillsIdDto = request.getSkills();

            AtomicReference<StudentEntity> studentAtomic = new AtomicReference<>(student);

            List<SkillEntity> skills = skillsIdDto.stream()
                    .map(skill -> {
                        SkillEntity skillEntity = skillRepository.findById(skill)
                                .orElseThrow(() -> new EntityNotFoundException("Skill inexistente"));
                        skillEntity.addStudent(studentAtomic.get());
                        return skillEntity;
                    })
                    .collect(Collectors.toList());

            student.setSkills(skills);
        }

        if (!mailInviteEnabled) {
            student.setEnabled(true);
        }

        try {
            student = studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyRegisteredException("Email já cadastrado");
        }

        if (mailInviteEnabled) {
            createConfirmationEmailAndSend(student);
        }

        return ResponseEntity.ok().build();
    }

    public List<StudentEntity> listStudents() {
        List<StudentEntity> students = studentRepository.findAll();

        if (students.isEmpty()) {
            throw new NoContentException("No students registered");
        }

        return students;
    }

    public StudentEntity searchStudentById(UUID id) {
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(STUDENT_NOT_FOUND + id));

        return student;
    }

    public ResponseEntity<Object> deleteStudentById(UUID id, Authentication authentication) {
        // super.verifyAuthorization(authentication, id);

        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(STUDENT_NOT_FOUND + id));

        studentRepository.delete(student);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Object> updateStudent(UUID id, StudentRegisterRequest request,
            Authentication authentication) {
        // super.verifyAuthorization(authentication, id);

        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(STUDENT_NOT_FOUND + id));

        student.update(request);
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Object> createConfirmationEmailAndSend(UserEntity user) {
        EmailConfirmationTokenEntity email = createConfirmationEmail(user);
        emailService.sendEmailAsync(email);
        return ResponseEntity.ok().build();
    }

    private EmailConfirmationTokenEntity createConfirmationEmail(UserEntity user) {
        String token = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()),
                StandardCharsets.US_ASCII);
        EmailConfirmationTokenEntity emailConfirmationToken = new EmailConfirmationTokenEntity(token, user);

        return emailConfirmationTokenRepository.save(emailConfirmationToken);
    }

}

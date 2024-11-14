package com.lab.estagiou.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.lab.estagiou.dto.request.model.jobvacancy.JobVacancyRegisterRequest;
import com.lab.estagiou.exception.generic.NoContentException;
import com.lab.estagiou.exception.generic.NotFoundException;
import com.lab.estagiou.jwt.JwtUserDetails;
import com.lab.estagiou.model.company.CompanyEntity;
import com.lab.estagiou.model.company.CompanyRepository;
import com.lab.estagiou.model.course.CourseEntity;
import com.lab.estagiou.model.course.CourseRepository;
import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.model.jobvacancy.JobVacancyRepository;
import com.lab.estagiou.model.skill.SkillEntity;
import com.lab.estagiou.model.skill.SkillRepository;

@Service
public class JobVacancyService {

    @Autowired
    private JobVacancyRepository jobVacancyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static final String JOB_VACANCY_NOT_FOUND = "Job Vacancy not found: ";

    public JobVacancyEntity registerJobVacancy(JobVacancyRegisterRequest request, JwtUserDetails user) {
        CompanyEntity company = companyRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Company not found: " + user.getId()));

        List<SkillEntity> skillEntities = skillRepository.findAllById(request.getSkills());
        CourseEntity course = courseRepository.findById(request.getCourse())
                .orElseThrow(() -> new NotFoundException("Course not found: " + request.getCourse()));

        JobVacancyEntity jobVacancy = new JobVacancyEntity(request, company, skillEntities, course);

        jobVacancy = jobVacancyRepository.save(jobVacancy);

        List<SkillEntity> skillsSave = new ArrayList<>();
        for (SkillEntity skill : skillEntities) {
            skill.addJobVacancy(jobVacancy);
            skillsSave.add(skill);
        }
        skillRepository.saveAll(skillsSave);

        return jobVacancy;
    }

    public List<JobVacancyEntity> listJobVacancies() {
        List<JobVacancyEntity> jobVacancies = jobVacancyRepository.findAll();

        if (jobVacancies.isEmpty()) {
            throw new NoContentException("No job vacancies registered");
        }

        return jobVacancies;
    }

    public JobVacancyEntity searchJobVacancyById(UUID id) {
        JobVacancyEntity jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(JOB_VACANCY_NOT_FOUND + id));

        return jobVacancy;
    }

    public ResponseEntity<Object> deleteJobVacancyById(UUID id, Authentication authentication) {
        // if (authentication == null) {
        // throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT);
        // }

        JobVacancyEntity jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(JOB_VACANCY_NOT_FOUND + id));

        // if (!jobVacancy.equalsCompanyOrAdmin(authentication)) {
        // throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT_DOTS +
        // ((UserEntity) authentication.getPrincipal()).getId());
        // }

        jobVacancyRepository.deleteById(id);

        // log(LogEnum.INFO, "Job Vacancy deleted: " + id,
        // HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Object> updateJobVacancy(UUID id, JobVacancyRegisterRequest request,
            Authentication authentication) {
        // if (authentication == null) {
        // throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT);
        // }

        JobVacancyEntity jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(JOB_VACANCY_NOT_FOUND + id));

        // if (!jobVacancy.equalsCompanyOrAdmin(authentication)) {
        // throw new UnauthorizedUserException(UNAUTHORIZED_ACESS_ATTEMPT_DOTS +
        // ((UserEntity) authentication.getPrincipal()).getId());
        // }

        jobVacancy.update(request);
        jobVacancyRepository.save(jobVacancy);

        // log(LogEnum.INFO, "Job Vacancy updated: " + jobVacancy.getId(),
        // HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

}

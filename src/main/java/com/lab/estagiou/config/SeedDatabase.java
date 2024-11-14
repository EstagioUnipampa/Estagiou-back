package com.lab.estagiou.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.lab.estagiou.dto.request.model.admin.AdminRegisterRequest;
import com.lab.estagiou.dto.request.model.enrollment.EnrollmentRegisterRequest;
import com.lab.estagiou.dto.request.model.jobvacancy.JobVacancyRegisterRequest;
import com.lab.estagiou.dto.request.model.util.RequestAddress;
import com.lab.estagiou.model.admin.AdminEntity;
import com.lab.estagiou.model.admin.AdminRepository;
import com.lab.estagiou.model.company.CompanyEntity;
import com.lab.estagiou.model.company.CompanyRepository;
import com.lab.estagiou.model.course.CourseEntity;
import com.lab.estagiou.model.course.CourseRepository;
import com.lab.estagiou.model.enrollment.EnrollmentEntity;
import com.lab.estagiou.model.enrollment.EnrollmentRepository;
import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.model.jobvacancy.JobVacancyRepository;
import com.lab.estagiou.model.skill.SkillEntity;
import com.lab.estagiou.model.skill.SkillRepository;
import com.lab.estagiou.model.student.StudentEntity;
import com.lab.estagiou.model.student.StudentRepository;

@Configuration
@Profile("test")
public class SeedDatabase implements CommandLineRunner {

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private AdminRepository adminRepository;

        @Autowired
        private CompanyRepository companyRepository;

        @Autowired
        private JobVacancyRepository jobVacancyRepository;

        @Autowired
        private EnrollmentRepository enrollmentRepository;

        @Autowired
        private CourseRepository courseRepository;

        @Autowired
        private SkillRepository skillRepository;

        @Override
        public void run(String... args) throws Exception {

                List<SkillEntity> skillsCC = Arrays.asList(
                                new SkillEntity("JavaScript"),
                                new SkillEntity("Python"),
                                new SkillEntity("Java"),
                                new SkillEntity("Estrutura de Dados"),
                                new SkillEntity("Banco de Dados"),
                                new SkillEntity("Inteligência Artificial"),
                                new SkillEntity("Pensamento Crítico"),
                                new SkillEntity("Trabalho em Equipe"));

                List<SkillEntity> skillsES = Arrays.asList(
                                new SkillEntity("React Native"),
                                new SkillEntity("TypeScript"),
                                new SkillEntity("Desenvolvimento Web"),
                                new SkillEntity("Arquitetura de Software"),
                                new SkillEntity("Controle de Versão"),
                                new SkillEntity("Testes Automatizados"),
                                new SkillEntity("Comunicação Eficaz"),
                                new SkillEntity("Resolução de Conflitos"));

                List<SkillEntity> skillsEC = Arrays.asList(
                                new SkillEntity("AutoCAD"),
                                new SkillEntity("SketchUp"),
                                new SkillEntity("Planejamento Urbano"),
                                new SkillEntity("Análise Estrutural"),
                                new SkillEntity("Gestão de Obras"),
                                new SkillEntity("Soluções Sustentáveis"),
                                new SkillEntity("Resolução de Problemas"),
                                new SkillEntity("Trabalho em Equipe"));

                skillsCC = skillRepository.saveAll(skillsCC);
                skillsES = skillRepository.saveAll(skillsES);
                skillsEC = skillRepository.saveAll(skillsEC);

                CourseEntity course1 = new CourseEntity("Engenharia de Software", skillsES);
                CourseEntity course2 = new CourseEntity("Ciência da Computação", skillsCC);
                CourseEntity course3 = new CourseEntity("Engenharia Civil", skillsEC);

                courseRepository.saveAll(Arrays.asList(course1, course2, course3));

                // Atualiza o curso de cada habilidade
                skillsES.forEach(skill -> skill.setCourse(course1));
                skillsCC.forEach(skill -> skill.setCourse(course2));
                skillsEC.forEach(skill -> skill.setCourse(course3));

                // Salva as habilidades novamente com a associação ao curso preenchida
                skillRepository.saveAll(skillsES);
                skillRepository.saveAll(skillsCC);
                skillRepository.saveAll(skillsEC);

                List<SkillEntity> skills = skillsES.subList(0, 6);
                SkillEntity skill1 = skills.get(0);
                SkillEntity skill2 = skills.get(1);
                SkillEntity skill3 = skills.get(2);
                SkillEntity skill4 = skills.get(3);
                SkillEntity skill5 = skills.get(4);
                SkillEntity skill6 = skills.get(5);

                // Criação de estudantes, administradores, empresas, vagas e inscrições
                StudentEntity student1 = new StudentEntity("Ricardo", "Costa", "ricardo@gmail.com", "123456", course1);
                StudentEntity student2 = new StudentEntity("João", "Medina", "joao@gmail.com", "123456", course2);
                StudentEntity student3 = new StudentEntity("Tales", "Soares", "tales@gmail.com", "123456", course1);
                StudentEntity student4 = new StudentEntity("Emanuel", "Rosa", "emanuel@gmail.com", "123456", course2);

                studentRepository.saveAll(Arrays.asList(student1, student2, student3, student4));

                student1.setSkills(Arrays.asList(skill1, skill2, skill3, skill4));
                student2.setSkills(Arrays.asList(skill2, skill3, skill4, skill5));
                student3.setSkills(Arrays.asList(skill3, skill4, skill5, skill6));
                student4.setSkills(Arrays.asList(skill1, skill3, skill5, skill6));
                studentRepository.save(student1);

                skill1.setStudents(Arrays.asList(student1, student4));
                skill2.setStudents(Arrays.asList(student1, student2));
                skill3.setStudents(Arrays.asList(student1, student2, student3, student4));
                skill4.setStudents(Arrays.asList(student1, student2, student3));
                skill5.setStudents(Arrays.asList(student2, student3, student4));
                skill6.setStudents(Arrays.asList(student3, student4));
                skillRepository.saveAll(Arrays.asList(skill1, skill2, skill3, skill4, skill5, skill6));

                AdminRegisterRequest admin = new AdminRegisterRequest();
                admin.setEmail("admin@gmail.com");
                admin.setName("Admin");
                admin.setPassword("123456");

                AdminEntity admin1 = new AdminEntity(admin, true);
                adminRepository.save(admin1);

                RequestAddress requestAddress = new RequestAddress("Brasil", "Rio Grande do Sul", "Alegrete", "Centro",
                                "Rua dos Andradas", "123", "Sala 302");

                CompanyEntity company1 = new CompanyEntity("Company 1", "company@gmail.com", "123456", "12345678901234",
                                "Accountable 1", requestAddress);
                companyRepository.save(company1);

                JobVacancyRegisterRequest jobVacancyRegisterRequest1 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest1.setTitle("Desenvolvedor Java");
                jobVacancyRegisterRequest1.setRole("Desenvolvimento de sistemas");
                jobVacancyRegisterRequest1.setDescription("Desenvolvimento de sistemas em Java");
                jobVacancyRegisterRequest1.setSalary("2000.0");
                jobVacancyRegisterRequest1.setHours("40");
                jobVacancyRegisterRequest1.setModality("Remoto");

                JobVacancyRegisterRequest jobVacancyRegisterRequest2 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest2.setTitle("Analista de Dados");
                jobVacancyRegisterRequest2.setRole("Análise de dados");
                jobVacancyRegisterRequest2.setDescription("Análise de grandes volumes de dados");
                jobVacancyRegisterRequest2.setSalary("3000.0");
                jobVacancyRegisterRequest2.setHours("48");
                jobVacancyRegisterRequest2.setModality("Presencial");

                JobVacancyRegisterRequest jobVacancyRegisterRequest3 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest3.setTitle("Engenheiro de Software");
                jobVacancyRegisterRequest3.setRole("Desenvolvimento de software");
                jobVacancyRegisterRequest3.setDescription("Desenvolvimento de software escalável");
                jobVacancyRegisterRequest3.setSalary("5000.0");
                jobVacancyRegisterRequest3.setHours("40");
                jobVacancyRegisterRequest3.setModality("Híbrido");

                JobVacancyRegisterRequest jobVacancyRegisterRequest4 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest4.setTitle("Desenvolvedor Frontend");
                jobVacancyRegisterRequest4.setRole("Desenvolvimento web");
                jobVacancyRegisterRequest4.setDescription("Desenvolvimento de interfaces web");
                jobVacancyRegisterRequest4.setSalary("3500.0");
                jobVacancyRegisterRequest4.setHours("48");
                jobVacancyRegisterRequest4.setModality("Remoto");

                JobVacancyRegisterRequest jobVacancyRegisterRequest5 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest5.setTitle("Desenvolvedor Backend");
                jobVacancyRegisterRequest5.setRole("Desenvolvimento de APIs");
                jobVacancyRegisterRequest5.setDescription("Desenvolvimento de APIs REST");
                jobVacancyRegisterRequest5.setSalary("4000.0");
                jobVacancyRegisterRequest5.setHours("40");
                jobVacancyRegisterRequest5.setModality("Presencial");

                JobVacancyRegisterRequest jobVacancyRegisterRequest6 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest6.setTitle("DevOps Engineer");
                jobVacancyRegisterRequest6.setRole("Automação de infraestrutura");
                jobVacancyRegisterRequest6.setDescription("Gerenciamento de pipelines e infraestrutura");
                jobVacancyRegisterRequest6.setSalary("4500.0");
                jobVacancyRegisterRequest6.setHours("36");
                jobVacancyRegisterRequest6.setModality("Remoto");

                JobVacancyRegisterRequest jobVacancyRegisterRequest7 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest7.setTitle("Especialista em Segurança da Informação");
                jobVacancyRegisterRequest7.setRole("Segurança cibernética");
                jobVacancyRegisterRequest7.setDescription("Gestão e implementação de segurança da informação");
                jobVacancyRegisterRequest7.setSalary("6000.0");
                jobVacancyRegisterRequest7.setHours("40");
                jobVacancyRegisterRequest7.setModality("Híbrido");

                JobVacancyRegisterRequest jobVacancyRegisterRequest8 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest8.setTitle("Analista de Suporte");
                jobVacancyRegisterRequest8.setRole("Suporte técnico");
                jobVacancyRegisterRequest8.setDescription("Atendimento e suporte ao usuário final");
                jobVacancyRegisterRequest8.setSalary("2500.0");
                jobVacancyRegisterRequest8.setHours("34");
                jobVacancyRegisterRequest8.setModality("Presencial");

                JobVacancyRegisterRequest jobVacancyRegisterRequest9 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest9.setTitle("Desenvolvedor Full Stack");
                jobVacancyRegisterRequest9.setRole("Desenvolvimento web completo");
                jobVacancyRegisterRequest9.setDescription("Desenvolvimento de front e backend");
                jobVacancyRegisterRequest9.setSalary("5500.0");
                jobVacancyRegisterRequest9.setHours("32");
                jobVacancyRegisterRequest9.setModality("Remoto");

                JobVacancyRegisterRequest jobVacancyRegisterRequest10 = new JobVacancyRegisterRequest();
                jobVacancyRegisterRequest10.setTitle("Product Manager");
                jobVacancyRegisterRequest10.setRole("Gestão de produto");
                jobVacancyRegisterRequest10.setDescription("Gerenciamento do ciclo de vida de produtos digitais");
                jobVacancyRegisterRequest10.setSalary("7000.0");
                jobVacancyRegisterRequest10.setHours("40");
                jobVacancyRegisterRequest10.setModality("Híbrido");

                JobVacancyEntity jobVacancy1 = new JobVacancyEntity(jobVacancyRegisterRequest1, company1,
                                Arrays.asList(skill1, skill2, skill3, skill4),
                                course1);

                JobVacancyEntity jobVacancy2 = new JobVacancyEntity(jobVacancyRegisterRequest2, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy3 = new JobVacancyEntity(jobVacancyRegisterRequest3, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy4 = new JobVacancyEntity(jobVacancyRegisterRequest4, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy5 = new JobVacancyEntity(jobVacancyRegisterRequest5, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy6 = new JobVacancyEntity(jobVacancyRegisterRequest6, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy7 = new JobVacancyEntity(jobVacancyRegisterRequest7, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy8 = new JobVacancyEntity(jobVacancyRegisterRequest8, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy9 = new JobVacancyEntity(jobVacancyRegisterRequest9, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);
                JobVacancyEntity jobVacancy10 = new JobVacancyEntity(jobVacancyRegisterRequest10, company1,
                                Arrays.asList(skill1, skill2, skill3,
                                                skill4),
                                course1);

                List<JobVacancyEntity> jobVacancyEntities = jobVacancyRepository
                                .saveAll(Arrays.asList(jobVacancy1, jobVacancy2, jobVacancy3, jobVacancy4, jobVacancy5,
                                                jobVacancy6, jobVacancy7, jobVacancy8, jobVacancy9, jobVacancy10));

                skill1.setJobVacancies(jobVacancyEntities);
                skill2.setJobVacancies(jobVacancyEntities);
                skill3.setJobVacancies(jobVacancyEntities);
                skill4.setJobVacancies(jobVacancyEntities);

                skillRepository.saveAll(Arrays.asList(skill1, skill2, skill3, skill4));

                EnrollmentRegisterRequest enrollmentRegisterRequest1 = new EnrollmentRegisterRequest();
                enrollmentRegisterRequest1.setStudentId(student1.getId());
                enrollmentRegisterRequest1.setJobVacancyId(jobVacancy1.getId());

                EnrollmentEntity enrollment1 = new EnrollmentEntity(enrollmentRegisterRequest1, student1, jobVacancy1);

                EnrollmentRegisterRequest enrollmentRegisterRequest2 = new EnrollmentRegisterRequest();
                enrollmentRegisterRequest2.setStudentId(student2.getId());
                enrollmentRegisterRequest2.setJobVacancyId(jobVacancy2.getId());

                EnrollmentRegisterRequest enrollmentRegisterRequest3 = new EnrollmentRegisterRequest();
                enrollmentRegisterRequest3.setStudentId(student2.getId());
                enrollmentRegisterRequest3.setJobVacancyId(jobVacancy3.getId());

                EnrollmentEntity enrollment2 = new EnrollmentEntity(enrollmentRegisterRequest2, student2, jobVacancy2);
                EnrollmentEntity enrollment3 = new EnrollmentEntity(enrollmentRegisterRequest3, student2, jobVacancy3);

                enrollmentRepository.saveAll(Arrays.asList(enrollment1, enrollment2, enrollment3));

        }

}

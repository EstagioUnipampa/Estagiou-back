package com.lab.estagiou.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lab.estagiou.controller.util.UtilController;
import com.lab.estagiou.dto.request.model.jobvacancy.JobVacancyRegisterRequest;
import com.lab.estagiou.dto.response.error.ErrorResponse;
import com.lab.estagiou.dto.response.job_vacancy.JobVacancyResponse;
import com.lab.estagiou.dto.response.job_vacancy.JobVacancyResponseEnrollment;
import com.lab.estagiou.jwt.JwtUserDetails;
import com.lab.estagiou.model.jobvacancy.JobVacancyEntity;
import com.lab.estagiou.service.EnrollmentService;
import com.lab.estagiou.service.JobVacancyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping(value = UtilController.API_VERSION + "/jobvacancy", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Job Vacancy", description = "API for management of job vacancies")
public class JobVacancyController {

    @Autowired
    private JobVacancyService jobVacancyService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Operation(summary = "Register job vacancy", description = "Register a job vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job vacancy registered successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @PostMapping
    public ResponseEntity<JobVacancyResponse> registerJobVacancy(@RequestBody JobVacancyRegisterRequest request,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        JobVacancyEntity jobVacancyEntity = jobVacancyService.registerJobVacancy(request, jwtUserDetails);
        JobVacancyResponse response = new JobVacancyResponse(jobVacancyEntity);

        return ResponseEntity.created(URI.create("/jobvacancy/" + response.getId())).body(response);
    }

    @Operation(summary = "List job vacancies", description = "List all job vacancies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job vacancies listed successfully"),
            @ApiResponse(responseCode = "204", description = "No job vacancies found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<List<JobVacancyResponse>> listJobVacancies() {
        List<JobVacancyEntity> jobVacancies = jobVacancyService.listJobVacancies();
        List<JobVacancyResponse> response = JobVacancyResponse.fromEntityList(jobVacancies);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search job vacancy by ID", description = "Search a job vacancy by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job vacancy found successfully", content = @Content(schema = @Schema(implementation = JobVacancyEntity.class))),
            @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job vacancy not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY', 'ROLE_STUDENT')")
    public ResponseEntity<JobVacancyResponse> searchJobVacancyById(@PathVariable UUID id,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        JobVacancyEntity jobVacancy = jobVacancyService.searchJobVacancyById(id);
        boolean isEnroll = enrollmentService.isEnroll(jwtUserDetails.getId(), id);
        JobVacancyResponse response = new JobVacancyResponse(jobVacancy, isEnroll);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search job vacancy by ID", description = "Search a job vacancy by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job vacancy found successfully", content = @Content(schema = @Schema(implementation = JobVacancyEntity.class))),
            @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job vacancy not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @GetMapping("/{id}/enrollments")
    public ResponseEntity<JobVacancyResponseEnrollment> searchJobVacancyEnrollmentsById(@PathVariable UUID id) {
        JobVacancyEntity jobVacancy = jobVacancyService.searchJobVacancyById(id);
        JobVacancyResponseEnrollment response = new JobVacancyResponseEnrollment(jobVacancy);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete job vacancy by ID", description = "Delete a job vacancy by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job vacancy deleted successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job vacancy not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteJobVacancyById(@PathVariable UUID id, Authentication authentication) {
        return jobVacancyService.deleteJobVacancyById(id, authentication);
    }

    @Operation(summary = "Update job vacancy", description = "Update a job vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job vacancy updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Incorrect atributtes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Job vacancy not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateJobVacancy(@PathVariable UUID id,
            @RequestBody JobVacancyRegisterRequest request, Authentication authentication) {
        return jobVacancyService.updateJobVacancy(id, request, authentication);
    }

}

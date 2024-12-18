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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lab.estagiou.controller.util.UtilController;
import com.lab.estagiou.dto.request.model.enrollment.EnrollmentPatchRequest;
import com.lab.estagiou.dto.request.model.enrollment.EnrollmentRegisterRequest;
import com.lab.estagiou.dto.response.enrollment.EnrollmentResponse;
import com.lab.estagiou.dto.response.enrollment.ResponseStatusDecided;
import com.lab.estagiou.dto.response.error.ErrorResponse;
import com.lab.estagiou.jwt.JwtUserDetails;
import com.lab.estagiou.model.enrollment.EnrollmentEntity;
import com.lab.estagiou.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping(value = UtilController.API_VERSION + "/enrollment", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Enrollment", description = "API for management of enrollments")
public class EnrollmentController {

        @Autowired
        private EnrollmentService enrollmentService;

        @Operation(summary = "Register enrollment", description = "Register an enrollment")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Enrollment registered successfully"),
                        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/register")
        public ResponseEntity<EnrollmentResponse> registerEnrollment(@RequestBody EnrollmentRegisterRequest request,
                        @AuthenticationPrincipal JwtUserDetails userDetails) {
                EnrollmentEntity enrollmentEntity = enrollmentService.registerEnrollment(request, userDetails);
                EnrollmentResponse enrollmentResponse = new EnrollmentResponse(enrollmentEntity);

                return ResponseEntity.created(URI.create("/enrollment/" + enrollmentEntity.getId()))
                                .body(enrollmentResponse);
        }

        @Operation(summary = "List enrollments", description = "List all enrollments")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Enrollments listed successfully"),
                        @ApiResponse(responseCode = "204", description = "No enrollments found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COMPANY', 'ROLE_STUDENT')")
        @GetMapping("/list")
        public ResponseEntity<List<EnrollmentResponse>> listEnrollments() {
                List<EnrollmentEntity> enrollments = enrollmentService.listEnrollments();
                List<EnrollmentResponse> enrollmentsResponse = EnrollmentResponse.convertList(enrollments);

                return ResponseEntity.ok(enrollmentsResponse);
        }

        @Operation(summary = "Search enrollment by ID", description = "Search an enrollment by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Enrollment found successfully", content = @Content(schema = @Schema(implementation = EnrollmentEntity.class))),
                        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Enrollment not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PreAuthorize("hasAnyRole('ROLE_STUDENT')")
        @GetMapping("/student")
        public ResponseEntity<List<EnrollmentResponse>> searchEnrollmentById(
                        @AuthenticationPrincipal JwtUserDetails userDetails) {
                List<EnrollmentEntity> enrollments = enrollmentService
                                .searchStudentsEnrollmentsById(userDetails.getId());
                List<EnrollmentResponse> enrollmentsResponse = EnrollmentResponse.convertList(enrollments, true);

                return ResponseEntity.ok(enrollmentsResponse);
        }

        @Operation(summary = "Search enrollment by ID", description = "Search an enrollment by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Enrollment found successfully", content = @Content(schema = @Schema(implementation = EnrollmentEntity.class))),
                        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Enrollment not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<Object> searchEnrollmentById(@PathVariable UUID id) {
                return enrollmentService.searchEnrollmentById(id);
        }

        @Operation(summary = "Search enrollment file by ID", description = "Search an enrollment file by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Enrollment file found successfully", content = @Content(schema = @Schema(implementation = EnrollmentEntity.class))),
                        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Enrollment file not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping("/file/{id}")
        public ResponseEntity<Object> searchEnrollmentFileById(@PathVariable UUID id) {
                return enrollmentService.searchEnrollmentFileById(id);
        }

        @Operation(summary = "Delete enrollment by ID", description = "Delete an enrollment by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Enrollment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Object> deleteEnrollmentById(@PathVariable UUID id, Authentication authentication) {
                return enrollmentService.deleteEnrollmentById(id, authentication);
        }

        @PatchMapping("/{idVacancy}/{idStudent}")
        @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
        public ResponseEntity<EnrollmentResponse> updateEnrollmentStatus(@PathVariable UUID idVacancy,
                        @PathVariable UUID idStudent,
                        @RequestBody EnrollmentPatchRequest request) {

                EnrollmentEntity enrollmentEntity = enrollmentService.updateEnrollmentStatus(idVacancy, idStudent,
                                request.isStatus());
                EnrollmentResponse enrollmentResponse = new EnrollmentResponse(enrollmentEntity);

                return ResponseEntity.ok(enrollmentResponse);
        }

        @GetMapping("/statusdecided/{idVacancy}/{idStudent}")
        @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
        public ResponseEntity<ResponseStatusDecided> getStatusDecided(@PathVariable UUID idVacancy,
                        @PathVariable UUID idStudent) {

                boolean statusDecided = enrollmentService.statusDecided(idVacancy, idStudent);

                return ResponseEntity.ok(new ResponseStatusDecided(statusDecided));
        }

}

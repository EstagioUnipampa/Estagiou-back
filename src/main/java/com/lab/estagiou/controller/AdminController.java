package com.lab.estagiou.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lab.estagiou.controller.util.UtilController;
import com.lab.estagiou.dto.request.model.admin.AdminRegisterRequest;
import com.lab.estagiou.dto.response.error.ErrorResponse;
import com.lab.estagiou.model.admin.AdminEntity;
import com.lab.estagiou.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping(value = UtilController.API_VERSION + "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin", description = "API for management of admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Register admin", description = "Register an admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin registered successfully"),
        @ApiResponse(responseCode = "400", description = "Email already registered", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<Object> registerAdmin(@RequestBody AdminRegisterRequest request) {
        return adminService.registerAdmin(request);
    }

    @Operation(summary = "List admins", description = "List all admins")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admins listed successfully"),
        @ApiResponse(responseCode = "204", description = "No admins found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminEntity>> listAdmins(Authentication authentication) {
        return adminService.listAdmins();
    }

    @Operation(summary = "Search admin by ID", description = "Search an admin by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin found successfully", content = @Content(schema = @Schema(implementation = AdminEntity.class))),
        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> searchAdminById(@PathVariable UUID id, Authentication authentication) {
        return adminService.searchAdminById(id, authentication);
    }

    @Operation(summary = "Delete admin by ID", description = "Delete an admin by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Admin deleted successfully", content = @Content),
        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAdminById(@PathVariable UUID id, Authentication authentication) {
        return adminService.deleteAdminById(id, authentication);
    }

    @Operation(summary = "Update admin", description = "Update an admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Admin updated successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Incorrect atributtes", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Authentication expired", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAdmin(@PathVariable UUID id, @RequestBody AdminRegisterRequest requestCadastro, Authentication authentication) {
        return adminService.updateAdmin(id, requestCadastro, authentication);
    }
    
}

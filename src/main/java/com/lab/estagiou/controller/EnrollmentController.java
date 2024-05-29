package com.lab.estagiou.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lab.estagiou.controller.util.UtilController;
import com.lab.estagiou.dto.request.model.enrollment.EnrollmentRegisterRequest;
import com.lab.estagiou.service.EnrollmentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping(value = UtilController.API_VERSION + "/enrollment", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Company", description = "API for management of companies")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerEnrollment(@ModelAttribute EnrollmentRegisterRequest request) {
        return enrollmentService.registerEnrollment(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEnrollment(@PathVariable UUID id) {
        return enrollmentService.getEnrollment(id);
    }
    
}

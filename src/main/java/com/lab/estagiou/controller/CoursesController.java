package com.lab.estagiou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lab.estagiou.controller.util.UtilController;
import com.lab.estagiou.dto.response.course.CourseResponseDto;
import com.lab.estagiou.model.course.CourseEntity;
import com.lab.estagiou.service.CourseService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping(value = UtilController.API_VERSION + "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Course", description = "API for management of courses")
public class CoursesController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> findAll() {
        List<CourseEntity> courses = courseService.findAll();
        List<CourseResponseDto> coursesDto = CourseResponseDto.toDto(courses);
        return ResponseEntity.ok(coursesDto);
    }

}

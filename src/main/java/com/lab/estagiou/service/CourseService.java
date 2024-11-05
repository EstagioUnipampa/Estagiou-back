package com.lab.estagiou.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab.estagiou.model.course.CourseEntity;
import com.lab.estagiou.model.course.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<CourseEntity> findAll() {
        return courseRepository.findAll();
    }

}

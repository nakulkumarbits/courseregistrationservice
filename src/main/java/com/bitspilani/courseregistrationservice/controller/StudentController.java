package com.bitspilani.courseregistrationservice.controller;

import com.bitspilani.courseregistrationservice.service.CourseRegistrationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    private final CourseRegistrationService courseRegistrationService;

    @Autowired
    public StudentController(CourseRegistrationService courseRegistrationService) {
        this.courseRegistrationService = courseRegistrationService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Long>> getCoursesForStudent(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(courseRegistrationService.getCoursesForStudent(studentId));
    }
}

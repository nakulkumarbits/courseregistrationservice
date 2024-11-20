package com.bitspilani.courseregistrationservice.controller;

import com.bitspilani.courseregistrationservice.dto.CourseRegistrationDTO;
import com.bitspilani.courseregistrationservice.dto.ErrorResponse;
import com.bitspilani.courseregistrationservice.service.CourseRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class CourseRegistrationController {

    private final CourseRegistrationService courseRegistrationService;

    @Autowired
    public CourseRegistrationController(CourseRegistrationService courseRegistrationService) {
        this.courseRegistrationService = courseRegistrationService;
    }

    @PostMapping
    public ResponseEntity<CourseRegistrationDTO> addRegistration(
        @RequestBody CourseRegistrationDTO courseRegistrationDTO) {
        return ResponseEntity.ok(courseRegistrationService.addRegistration(courseRegistrationDTO));
    }

    @DeleteMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Void> removeRegistration(@PathVariable("studentId") Long studentId,
        @PathVariable("courseId") Long courseId) {
        courseRegistrationService.removeRegistration(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Boolean> checkIfStudentRegistered(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(courseRegistrationService.isStudentRegistered(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Boolean> checkIfCourseRegistered(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(courseRegistrationService.isCourseRegistered(courseId));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(DataIntegrityViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Student already registered for the course", null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
        if (ex.getMessage().equals("Course is not registered or service unavailable") || ex.getMessage()
            .equals("Student not found or service unavailable")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

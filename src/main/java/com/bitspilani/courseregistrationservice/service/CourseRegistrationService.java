package com.bitspilani.courseregistrationservice.service;

import com.bitspilani.courseregistrationservice.dto.CourseRegistrationDTO;
import com.bitspilani.courseregistrationservice.model.CourseRegistration;
import com.bitspilani.courseregistrationservice.repository.CourseRegistrationRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CourseRegistrationService {

    public static final String USERS = "/admin/get-users/";
    @Value("${user.service.url}")
    private String userServiceUrl;

    public static final String COURSES = "/courses/";
    @Value("${course.service.url}")
    private String courseServiceUrl;

    private final CourseRegistrationRepository courseRegistrationRepository;
    private final RestTemplate restTemplate;

    public CourseRegistrationService(CourseRegistrationRepository courseRegistrationRepository,
        RestTemplate restTemplate) {
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.restTemplate = restTemplate;
    }

    public CourseRegistrationDTO addRegistration(CourseRegistrationDTO courseRegistrationDTO) {

//        checkIfStudentExists(courseRegistrationDTO.getStudentId());
//        checkIfCourseExists(courseRegistrationDTO.getCourseId());


        CourseRegistration courseRegistration = getCourseRegistration(courseRegistrationDTO);
        CourseRegistration registration = courseRegistrationRepository.save(courseRegistration);
        return getCourseRegistrationDTO(registration);
    }

    private void checkIfCourseExists(Long courseId) {
        String getCourseEndpoint = StringUtils.join(courseServiceUrl, COURSES, courseId);
        ResponseEntity<Object> response = restTemplate.getForEntity(getCourseEndpoint, Object.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Course not found or service unavailable");
        }
    }

    private void checkIfStudentExists(Long studentId) {
        String getUserEndpoint = StringUtils.join(userServiceUrl, USERS, studentId);
        ResponseEntity<Object> response = restTemplate.getForEntity(getUserEndpoint, Object.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Student not found or service unavailable");
        }
    }

    public void removeRegistration(Long studentId, Long courseId) {
        courseRegistrationRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    public Boolean isStudentRegistered(Long studentId) {
        return courseRegistrationRepository.existsByStudentId(studentId);
    }

    public Boolean isCourseRegistered(Long courseId) {
        return courseRegistrationRepository.existsByCourseId(courseId);
    }

    public List<Long> getCoursesForStudent(Long studentId) {
        return courseRegistrationRepository.findByStudentId(studentId)
            .stream()
            .map(CourseRegistration::getCourseId)
            .toList();
    }

    private CourseRegistrationDTO getCourseRegistrationDTO(CourseRegistration registration) {
        CourseRegistrationDTO courseRegistrationDTO = new CourseRegistrationDTO();
        courseRegistrationDTO.setStudentId(registration.getStudentId());
        courseRegistrationDTO.setCourseId(registration.getCourseId());
        courseRegistrationDTO.setRegistrationDate(registration.getCreatedDate());
        return courseRegistrationDTO;
    }

    private static CourseRegistration getCourseRegistration(CourseRegistrationDTO courseRegistrationDTO) {
        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setStudentId(courseRegistrationDTO.getStudentId());
        courseRegistration.setCourseId(courseRegistrationDTO.getCourseId());
        return courseRegistration;
    }
}

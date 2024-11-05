package com.bitspilani.courseregistrationservice.repository;

import com.bitspilani.courseregistrationservice.model.CourseRegistration;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {

    @Transactional
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentId(Long studentId);

    boolean existsByCourseId(Long courseId);

    List<CourseRegistration> findByStudentId(Long studentId);
}

package com.bitspilani.courseregistrationservice.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CourseRegistrationDTO {

    private Long studentId;
    private Long courseId;
    private LocalDateTime registrationDate;
}

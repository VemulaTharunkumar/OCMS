package com.example.demo.Model;

import com.example.demo.Enumeration.EnrollmentStatus;
import jakarta.persistence.*;
import com.example.demo.Model.Course;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Enrollment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "student_id_rno")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalTime enrolledAt;

    private EnrollmentStatus status = EnrollmentStatus.ONGOING;

    @PrePersist
    public void setEnrolledAt() {
        if (enrolledAt == null) {
            enrolledAt = LocalTime.now();
        }
    }
}

package com.example.demo.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "learning_progress",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"enrollment_id", "content_id"}
        )
)
@Getter
@Setter
public class LearningProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private CourseContent content;

    private boolean completed = false;

    private LocalDateTime completedAt;
}


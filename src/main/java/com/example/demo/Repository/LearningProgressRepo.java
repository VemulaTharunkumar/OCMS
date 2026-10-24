package com.example.demo.Repository;

import com.example.demo.Model.LearningProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearningProgressRepo extends JpaRepository<LearningProgress,Long> {
    Optional<Object> findByEnrollment_IdAndContent_Id(long enrollmentId, long contentId);
}

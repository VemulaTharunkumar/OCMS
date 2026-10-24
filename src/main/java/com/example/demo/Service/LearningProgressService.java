
package com.example.demo.Service;

import com.example.demo.Model.LearningProgress;
import com.example.demo.Repository.LearningProgressRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LearningProgressService {

    private final LearningProgressRepo progressRepo;

    public LearningProgressService(LearningProgressRepo progressRepo) {
        this.progressRepo = progressRepo;
    }

    @Transactional
    public LearningProgress tickbyContent(long enrollmentId, long contentId) {

        LearningProgress progress = (LearningProgress) progressRepo
                .findByEnrollment_IdAndContent_Id(enrollmentId, contentId)
                .orElseThrow(() ->
                        new RuntimeException("Learning progress not found"));

        progress.setCompleted(true);
        progress.setCompletedAt(java.time.LocalDateTime.now());

        return progressRepo.save(progress);
    }
}

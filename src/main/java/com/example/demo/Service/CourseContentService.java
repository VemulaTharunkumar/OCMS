
package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Repository.CourseContentRepo;
import com.example.demo.Repository.CourseRepo;
import com.example.demo.Repository.EnrollmentRepo;
import com.example.demo.Repository.LearningProgressRepo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CourseContentService {

    private final CourseContentRepo contentRepo;
    private final CourseRepo courseRepo;
    private final LearningProgressRepo lp;
    private final EnrollmentRepo enrollmentRepo;

    public CourseContentService(
            CourseContentRepo contentRepo,
            CourseRepo courseRepo,
            LearningProgressRepo lp,
            EnrollmentRepo enrollmentRepo) {

        this.contentRepo = contentRepo;
        this.courseRepo = courseRepo;
        this.lp = lp;
        this.enrollmentRepo = enrollmentRepo;
    }

    @Transactional
    public CourseContent addContent(
            Long courseId,
            CourseContentRequest request) {

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"));

        CourseContent content = new CourseContent();
        content.setCourse(course);
        content.setContenttype(request.getContenttype());
        content.setContenturl(request.getContenturl());
        content.setSequenceOrder(request.getSequenceOrder());

        CourseContent savedContent = contentRepo.save(content);

        List<Enrollment> enrollments =
                enrollmentRepo.findByCourse_Id(courseId);

        for (Enrollment enrollment : enrollments) {

            LearningProgress progress = new LearningProgress();
            progress.setEnrollment(enrollment);
            progress.setContent(savedContent);
            progress.setCompleted(false);

            lp.save(progress);
        }

        return savedContent;
    }

    public List<CourseContent> getContentByCourse(Long courseId) {

        if (!courseRepo.existsById(courseId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found");
        }

        return contentRepo
                .findByCourse_IdOrderBySequenceOrderAsc(courseId);
    }

    public List<CourseContent> getAllContent() {
        return contentRepo.findAll();
    }

    public CourseContent getContentById(Long id) {
        return contentRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course content not found"));
    }

    public CourseContent updateContent(
            Long id, CourseContent updatedContent) {

        CourseContent existingContent = contentRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course content not found"));

        existingContent.setContenttype(updatedContent.getContenttype());
        existingContent.setContenturl(updatedContent.getContenturl());
        existingContent.setSequenceOrder(
                updatedContent.getSequenceOrder());

        return contentRepo.save(existingContent);
    }

    public void deleteContent(Long id) {
        CourseContent existingContent = contentRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course content not found"));

        contentRepo.delete(existingContent);
    }
}

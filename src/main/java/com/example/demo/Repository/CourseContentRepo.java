package com.example.demo.Repository;

import com.example.demo.Model.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseContentRepo
        extends JpaRepository<CourseContent, Long> {

    List<CourseContent> findByCourse_IdOrderBySequenceOrderAsc(
            Long courseId);
}
package com.example.demo.Controller;

import java.util.List;

import com.example.demo.Model.CourseContentRequest;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.CourseContent;
import com.example.demo.Service.CourseContentService;

@RestController
@RequestMapping("/course-contents")
public class CourseContentController {

    private final CourseContentService courseContentService;

    public CourseContentController(CourseContentService courseContentService) {
        this.courseContentService = courseContentService;
    }

    @PostMapping("/course/{courseId}")
    public CourseContent addContent(
            @PathVariable Long courseId,
            @RequestBody CourseContentRequest content) {
        return courseContentService.addContent(courseId, content);
    }

    @GetMapping
    public List<CourseContent> getAllContent() {
        return courseContentService.getAllContent();
    }

    @GetMapping("/{id}")
    public CourseContent getContentById(@PathVariable Long id) {
        return courseContentService.getContentById(id);
    }

    @GetMapping("/course/{courseId}")
    public List<CourseContent> getContentByCourse(@PathVariable Long courseId) {
        return courseContentService.getContentByCourse(courseId);
    }

    @PutMapping("/{id}")
    public CourseContent updateContent(
            @PathVariable Long id,
            @RequestBody CourseContent updatedContent) {
        return courseContentService.updateContent(id, updatedContent);
    }

    @DeleteMapping("/{id}")
    public String deleteContent(@PathVariable Long id) {
        courseContentService.deleteContent(id);
        return "Content deleted successfully";
    }
}

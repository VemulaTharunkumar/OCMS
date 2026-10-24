package com.example.demo.Controller;

import java.util.List;

import com.example.demo.Model.InstructorApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.Course;
import com.example.demo.Service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/instructor")
    public ResponseEntity<?> createCourse(@RequestBody Course course,@RequestParam long id){
        try{
            Course c=courseService.saveCourse(course,id);
            return ResponseEntity.status(HttpStatus.CREATED).body(c);
        }
        catch (Exception e){
            if("Instructor Not Found".equals(e.getMessage())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            if("Instructor not Approved".equals(e.getMessage())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            throw e;
        }
    }
    @PutMapping("instructor/course/update")
    public ResponseEntity<?> courseUpdate(@RequestParam long id,@RequestParam long cid,@RequestBody Course course){
        try{
            courseService.updateCourseDetails(id,cid,course);
            return ResponseEntity.status(HttpStatus.OK).body(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/admin/course/change")
    public ResponseEntity<?> changeInstructor(@RequestParam long id,@RequestParam long courseId){
        try {
            Course c = courseService.updateCourse(id, courseId);
            return ResponseEntity.ok(c);
        }
        catch (RuntimeException rt) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rt.getMessage());
        }
    }
    @DeleteMapping("/instructor/course/delete")
    public ResponseEntity<?> deleteCourse(@RequestParam long id){
        String c=courseService.deleteCourse(id);
        if(c.equals("Course not found")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Deleted succesfully");
    }
}
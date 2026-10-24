package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.Enrollment;
import com.example.demo.Service.EnrollmentService;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<?> enrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {

        try{
            enrollmentService.enrollStudent(studentId, courseId);
            return ResponseEntity.ok("Enrollment Success");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with id not found");
        }
    }
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Enrollment getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getByStudent(
            @PathVariable Long studentId) {
        return enrollmentService.getByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<Enrollment> getByCourse(
            @PathVariable Long courseId) {
        return enrollmentService.getByCourse(courseId);
    }

    @PutMapping("/{id}/complete")
    public Enrollment completeEnrollment(@PathVariable Long id) {
        return enrollmentService.completeEnrollment(id);
    }
}

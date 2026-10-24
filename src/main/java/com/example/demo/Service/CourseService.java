package com.example.demo.Service;

import com.example.demo.Enumeration.ApprovalStatus;
import com.example.demo.Model.Course;
import com.example.demo.Model.InstructorApplication;
import com.example.demo.Repository.CourseRepo;
import com.example.demo.Repository.InstructorApplicationRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepo courseRepo;
    private final InstructorApplicationRepo instructorApplicationRepo;

    public CourseService(
            CourseRepo courseRepo,
            InstructorApplicationRepo instructorApplicationRepo) {
        this.courseRepo = courseRepo;
        this.instructorApplicationRepo = instructorApplicationRepo;
    }

    public Course saveCourse(Course course, Long id) {

        // Find the instructor application using its ID
        InstructorApplication instructor =
                instructorApplicationRepo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Instructor application not found"));

        // Check whether the instructor is approved
        if (instructor.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new RuntimeException("Instructor is not approved");
        }

        // Assign the instructor application to the course
        course.setInstructor(instructor);

        // Generate the course ID and save
        return courseRepo.save(course);
    }

    public Course updateCourse(long id,Long courseId) {
        Course c=courseRepo.findById(courseId).orElseThrow(()->new RuntimeException("Course not found"));
        InstructorApplication ip=instructorApplicationRepo.findById(id).orElseThrow(()->new RuntimeException("Instructor not found"));
        if(ip.getApprovalStatus()==ApprovalStatus.APPROVED) {
            c.setInstructor(ip);
        }
        else if(ip.getApprovalStatus()==ApprovalStatus.PENDING){
            ip.setApprovalStatus(ApprovalStatus.APPROVED);
            return courseRepo.save(c);
        }
        else{
            throw new RuntimeException("Rejected instructor not assigned");
        }
            return courseRepo.save(c);
    }

    public String deleteCourse(long id) {
        Optional<Course> c=courseRepo.findById(id);
        if(c.isEmpty()){
            return "Course not found";
        }
        courseRepo.deleteById(id);
        return "Course deleted Successfully";
    }

    public void updateCourseDetails(long id, long cid, Course course) {

        InstructorApplication ip = instructorApplicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor Not found"));

        Course c = courseRepo.findById(cid)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        long iid = c.getInstructor().getId();

        if (iid == id) {
            c.setTitle(course.getTitle());
            c.setDescription(course.getDescription());
            c.setCategory(course.getCategory());

            // Keep the existing instructor
            courseRepo.save(c);
        } else {
            throw new RuntimeException("You are not authorized to update this course");
        }
    }
}

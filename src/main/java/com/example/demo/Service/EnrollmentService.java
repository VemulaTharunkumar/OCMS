
package com.example.demo.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Model.User;
import com.example.demo.Model.Course;
import com.example.demo.Model.Enrollment;
import com.example.demo.Model.CourseContent;
import com.example.demo.Model.LearningProgress;

import com.example.demo.Repository.UserRepo;
import com.example.demo.Repository.CourseRepo;
import com.example.demo.Repository.EnrollmentRepo;
import com.example.demo.Repository.CourseContentRepo;
import com.example.demo.Repository.LearningProgressRepo;

import com.example.demo.Enumeration.Role;
import com.example.demo.Enumeration.EnrollmentStatus;

@Service
public class EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final CourseContentRepo contentRepo;
    private final LearningProgressRepo lp;
    private final NotificationService ns;

    public EnrollmentService(
            EnrollmentRepo enrollmentRepo,
            UserRepo userRepo,
            CourseRepo courseRepo,
            CourseContentRepo contentRepo,
            LearningProgressRepo lp,
            NotificationService ns) {

        this.enrollmentRepo = enrollmentRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.contentRepo = contentRepo;
        this.lp = lp;
        this.ns=ns;
    }

    // Enroll a student in a course
    @Transactional
    public Enrollment enrollStudent(Long studentId, Long courseId) {

        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found"));

        if (student.getRole() != Role.STUDENT) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only students can enroll in courses");
        }

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found"));

        if (enrollmentRepo.existsByStudent_IdAndCourse_Id(
                studentId, courseId)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ONGOING);

        // Save the enrollment first
        Enrollment savedEnrollment = enrollmentRepo.save(enrollment);
        String to=student.getEmail();
        String sub="Enrollment Information";
        String text="Hii "+student.getName()+","+"\n"+"You have Successfully Enrolled for the course "+course.getTitle()+"."+"With Instructor "+course.getInstructor().getName()+"."+"\n"+"Start completing the modules of course to finish the course"+"\n"+"Here is the details of your course "+"\n"+"Course Name:"+course.getTitle()+"\n"+"Course Description:"+course.getDescription()+"\n"+"Instructor:"+course.getInstructor().getName();
        ns.setMailSender(to,sub,text);
        String ito=course.getInstructor().getEmail();
        String isub="New Student Enrollment";
        String itext="Hey "+course.getInstructor().getName()+"\n"+"Student with id :"+student.getId()+"\n"+"Enrolled in the course you are instructing";
        ns.setMailSender(ito,isub,itext);
        // Get all content already assigned to this course
        List<CourseContent> contents =
                contentRepo.findByCourse_IdOrderBySequenceOrderAsc(courseId);

        // Create learning progress for every content item
        for (CourseContent content : contents) {

            LearningProgress progress = new LearningProgress();

            progress.setEnrollment(savedEnrollment);
            progress.setContent(content);
            progress.setCompleted(false);

            lp.save(progress);
        }

        return savedEnrollment;
    }

    // Get all enrollments
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepo.findAll();
    }

    // Get enrollment by ID
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Enrollment not found"));
    }

    public List<Enrollment> getByStudent(Long studentId) {
        return enrollmentRepo.findByStudent_Id(studentId);
    }

    public List<Enrollment> getByCourse(Long courseId) {
        return enrollmentRepo.findByCourse_Id(courseId);
    }

    // Mark an enrollment as completed
    public Enrollment completeEnrollment(Long id) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        return enrollmentRepo.save(enrollment);
    }
}

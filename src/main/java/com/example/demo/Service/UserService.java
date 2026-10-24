package com.example.demo.Service;

import com.example.demo.Enumeration.ApprovalStatus;
import com.example.demo.Enumeration.Role;
import com.example.demo.Model.Course;
import com.example.demo.Model.InstructorApplication;
import com.example.demo.Repository.CourseRepo;
import com.example.demo.Repository.InstructorApplicationRepo;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo ur;
    private final InstructorApplicationRepo ir;
    private final CourseRepo courseRepo;
    private final CourseService cs;
    private NotificationService ns;

    public UserService(UserRepo ur, InstructorApplicationRepo ir, CourseRepo courseRepo,CourseService cs,NotificationService ns) {
        this.ur = ur;
        this.ir = ir;
        this.courseRepo = courseRepo;
        this.cs=cs;
        this.ns=ns;
    }

    public User saveUser(User user) {

        String from=user.getEmail();
        String Sub="Registration Succesfull";
        String text= """
                Thank you for Choosing our Application to improve your learning.
                Our Application Contains 100's of Courses ,Browse them and Keep improving your Knowledge.
                Login to enroll in courses.
                ****Happy Learning*****s
                """;
        ns.setMailSender(from,Sub,text);
        return ur.save(user);
    }

    public InstructorApplication saveInstructor(InstructorApplication ip) {
        ip.setApprovalStatus(ApprovalStatus.PENDING);
        String to= ip.getEmail();
        String sub="Approval Status";
        String text="Your Application Sent to Admin for Access.We inform you when you get approval";
        ns.setMailSender(to,sub,text);
        return ir.save(ip);
    }

    public InstructorApplication updateInstructor(
            long id,long adminid) {
        User u=ur.findById(adminid).orElseThrow(()->new RuntimeException("Admin not found"));
        if(u.getRole()!=Role.ADMIN){
            throw new RuntimeException("Admin only allowed to approve");
        }
        InstructorApplication ip=ir.findById(id).orElseThrow(()->new RuntimeException("Instructor not found"));
        if(ip.getApprovalStatus()==ApprovalStatus.PENDING){
            ip.setApprovalStatus(ApprovalStatus.APPROVED);
            ns.setMailSender(ip.getEmail(), "Approval Status","You are Approved"+"\n"+"Now you can monitor courses add new courses");
        }
        else if(ip.getApprovalStatus()==ApprovalStatus.APPROVED){
            throw new RuntimeException("Instructor already approved");
        }
        else if(ip.getApprovalStatus()==ApprovalStatus.REJECTED){
            ip.setApprovalStatus(ApprovalStatus.APPROVED);
            ns.setMailSender(ip.getEmail(), "Approval Status","You are Approved"+"\n"+"Now you can monitor courses add new courses");
        }
        return ir.save(ip);
    }
    public InstructorApplication rejectInstructor(
            long id,long adminid) {
        User u=ur.findById(adminid).orElseThrow(()->new RuntimeException("Admin not found"));
        if(u.getRole()!=Role.ADMIN){
            throw new RuntimeException("Only Admin can reject the instructor");
        }
        InstructorApplication ip=ir.findById(id).orElseThrow(()->new RuntimeException("Instructor not found"));
        if(ip.getApprovalStatus()==ApprovalStatus.APPROVED){
            ip.setApprovalStatus(ApprovalStatus.REJECTED);
            ns.setMailSender(ip.getEmail(), "Approval Status","You are Rejected"+"\n"+"Only valid instructors get approval");
        } else if (ip.getApprovalStatus()==ApprovalStatus.REJECTED) {
            throw new RuntimeException("Instructor already Rejected");
        }
        else if(ip.getApprovalStatus()==ApprovalStatus.PENDING){
            ip.setApprovalStatus(ApprovalStatus.REJECTED);
            ns.setMailSender(ip.getEmail(), "Approval Status","You are Rejected"+"\n"+"Only valid instructors get approval");
        }
        return ir.save(ip);
    }
    //All courses view by student
    public List<Course> getCourses(long id) {
        User u=ur.findById(id).orElseThrow(()->new RuntimeException("Register to browse courses"));
        if(u.getRole()!= Role.STUDENT){
            throw new RuntimeException("You are not a Student");
        }
        List<Course>l=courseRepo.findAll();
        return l;
    }
    public List<Course> getCoursesByAdmin(long id) {
        User u=ur.findById(id).orElseThrow(()->new RuntimeException("Register to browse courses"));
        if(u.getRole()!= Role.ADMIN){
            throw new RuntimeException("You are not a Admin");
        }
        List<Course>l=courseRepo.findAll();
        return l;
    }

    public List<Course> getCoursesByInstructor(long insid) {
        InstructorApplication iy= ir.findById(insid).orElseThrow(()->new RuntimeException("Instructor not found"));
        if(iy.getApprovalStatus()==ApprovalStatus.APPROVED){
            List<Course>l=courseRepo.findByInstructor_Id(insid);
            return l;
        }
        else if(iy.getApprovalStatus()==ApprovalStatus.PENDING){
            throw new RuntimeException("Instructor not Approved");
        }
        else if(iy.getApprovalStatus()==ApprovalStatus.REJECTED){
            throw new RuntimeException("Instructor not allowed");
        }
        throw new RuntimeException("Invalid instructor approval status");
    }
    public void deleteUser(long id,long sid){
        User u=ur.findById(id).orElseThrow(()->new RuntimeException("Admin not found"));
        User u1=ur.findById(sid).orElseThrow(()->new RuntimeException("Student not found"));
        if(u.getRole()==Role.ADMIN){
            ur.deleteById(sid);
        }
        else{
            throw new RuntimeException("Admin only allowed to delete Student");
        }
    }
    public String deleteInstructor(
            long adminid, long iid, long newInstructorId) {

        User admin = ur.findById(adminid)
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException(
                    "Only admins can delete instructors");
        }

        InstructorApplication oldInstructor =
                ir.findById(iid)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Instructor not found"));

        if (iid == newInstructorId) {
            throw new RuntimeException(
                    "Choose a different instructor");
        }

        InstructorApplication newInstructor =
                ir.findById(newInstructorId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Replacement instructor not found"));

        if (newInstructor.getApprovalStatus()
                != ApprovalStatus.APPROVED) {
            throw new RuntimeException(
                    "Replacement instructor must be approved");
        }

        List<Course> courses =
                courseRepo.findByInstructor_Id(iid);

        for (Course course : courses) {
            cs.updateCourse(
                    newInstructorId, course.getId());
        }

        ir.delete(oldInstructor);

        return "Instructor deleted and courses reassigned successfully";
    }

    public User updateStudent(long id,User u) {
        User user=ur.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        user.setId(id);
        user.setName(u.getName());
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        user.setRole(Role.STUDENT);
        String to=user.getEmail();
        String Subject="Updation Info";
        String text="Details updated Succesfully"+"\n"+"\n"+"Happy Learning";
        ns.setMailSender(to,Subject,text);
        return ur.save(user);
    }

    public List<User> getStudents(long id) {
        User u=ur.findById(id).orElseThrow(()->new RuntimeException("Admin not found"));
        if(u.getRole()!=Role.ADMIN){
            throw new RuntimeException("Only admin can view all students");
        }
        List<User> l = ur.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.STUDENT)
                .toList();
        if (l.isEmpty()) {
            throw new RuntimeException("No students found");
        } else {
            return l;
        }
    }

    public User saveUserByAdmin(long id, User user) {
        User u=ur.findById(id).orElseThrow(()->new RuntimeException("Admin not found"));
        if(u.getRole()!=Role.ADMIN){
            throw new RuntimeException("Please use valid admin id");
        }
        return ur.save(user);
    }

    public String deleteStudent(long id) {
        User u=ur.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        if(u.getRole()!=Role.STUDENT){
            throw new RuntimeException("Not a valid Student");
        }
        ur.deleteById(id);
        return "Account Deleted Succesfully";
    }

    public InstructorApplication instructorCreation(long id,InstructorApplication ip) {
        User u=ur.findById(id).orElseThrow(()->new RuntimeException("Admin not found"));
        if(u.getRole()!=Role.ADMIN){
            throw new RuntimeException("Please create with valid id");
        }
        ip.setApprovalStatus(ApprovalStatus.APPROVED);
        return ir.save(ip);
    }

    public void updateInstructorDetails(long id, InstructorApplication ip) {
        InstructorApplication iapp=ir.findById(id).orElseThrow(()->new RuntimeException("Instructor not found"));
        long iid=ip.getId();
        if(iid!=id){
            iapp.setName(ip.getName());
            iapp.setEmail(ip.getEmail());
            iapp.setPassword(ip.getPassword());
            ir.save(iapp);
        }
        else{
            throw new RuntimeException("Given id not access the instructor details");
        }
    }
}
package com.example.demo.Controller;

import java.util.List;

import com.example.demo.Enumeration.Role;
import com.example.demo.Model.Course;
import com.example.demo.Model.InstructorApplication;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.CourseService;
import com.example.demo.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService us;

    public UserController(UserService us,CourseService cs,UserRepo up) {
        this.us = us;
    }
    //Student Registration
    @PostMapping("/register")
    public User createUser(@RequestBody User user){
        user.setRole(Role.STUDENT);
        return us.saveUser(user);
    }
    //Admin-studentRegistration
    @PostMapping("/admin/student/register")
    public ResponseEntity<?> createUserByAdmin(@RequestParam long id,@RequestBody User user){
        user.setRole(Role.STUDENT);
        try{
            us.saveUserByAdmin(id,user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //UpdateStudentDetails
    @PutMapping("/update/student")
    public ResponseEntity<?> updateStudent(@RequestParam long id,@RequestBody User u){
        try {
            us.updateStudent(id, u);
            return ResponseEntity.status(HttpStatus.OK).body(u);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //Get-allStudents
    @GetMapping("/admin/students")
    public ResponseEntity<?> getStudents(@RequestParam long id){
        try{
            List<User> l=us.getStudents(id);
            return ResponseEntity.status(HttpStatus.OK).body(l);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(e.getMessage());
        }
    }
    //DeleteStudents-Admin
    @DeleteMapping("/admin/delete/student")
    public ResponseEntity<?> deleteUser(@RequestParam long adminid,@RequestParam long sid){
        try{
            us.deleteUser(adminid,sid);
            return ResponseEntity.status(HttpStatus.OK).body("Student Deleted Succesfully");
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //DeleteByStudent
    @DeleteMapping("/studentDelete")
    public ResponseEntity<?> deleteStudent(@RequestParam long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(us.deleteStudent(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //InstructorCreation
    @PostMapping("/instructor/register")
    public InstructorApplication instructorRegister(@RequestBody InstructorApplication ip){
        return us.saveInstructor(ip);
    }
    @PutMapping("/admin/approval/{id}")
    public ResponseEntity<?> instructorApproval(@PathVariable long id,@RequestParam long adminid){
        try {
            us.updateInstructor(id, adminid);
            return ResponseEntity.status(HttpStatus.OK).body(us);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/admin/reject/{id}")
    public ResponseEntity<?> iap(@PathVariable long id, @RequestParam long adminid){
        try {
            us.rejectInstructor(id,adminid);
            return ResponseEntity.status(HttpStatus.OK).body(us);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //InstructorCreationByAdmin
    @PostMapping("/admin/instructor")
    public ResponseEntity<?> instructorCreation(@RequestParam long id,@RequestBody InstructorApplication ip) {
        try {
            us.instructorCreation(id, ip);
            return ResponseEntity.status(HttpStatus.OK).body(ip);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //Browse by student
    @GetMapping("/student/courses")
    public ResponseEntity<?> getCourses(@RequestParam  long id){
        try {
            List<Course> l= us.getCourses(id);
            return ResponseEntity.ok(l);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //Monitor by admin
    @GetMapping("/admin/courses")
    public ResponseEntity<?> getCoursesByAdmin(@RequestParam  long adminid){
        try {
            List<Course> l= us.getCoursesByAdmin(adminid);
            return ResponseEntity.ok(l);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //Browser by instructor
    @GetMapping("/instructor/courses")
    public ResponseEntity<?> getCoursesByInstructor(@RequestParam  long insid){
        try {
            List<Course> l= us.getCoursesByInstructor(insid);
            return ResponseEntity.ok(l);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //updateInstructor
    @PutMapping("/instructor/update")
    public ResponseEntity<?> updateInstructorDetails(@RequestParam long id,@RequestBody InstructorApplication ip){
        try{
            us.updateInstructorDetails(id,ip);
            return ResponseEntity.status(HttpStatus.OK).body(ip);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //DeleteInstructor
    @DeleteMapping("/admin/delete/instructor")
    public ResponseEntity<?> deleteInstructor(@RequestParam long adminid,@RequestParam long oldid,@RequestParam long newid){
        try{
            us.deleteInstructor(adminid,oldid,newid);
            return ResponseEntity.status(HttpStatus.OK).body("Instructor Deleted Succesfully");
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

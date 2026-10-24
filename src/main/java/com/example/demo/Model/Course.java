package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable=false)
    private String title;
    @Column(nullable = false,length = 2000)
    private String Description;
    private String Category;
    @ManyToOne
    @JoinColumn(name = "instructor_id",nullable=false)
    private InstructorApplication instructor;

}

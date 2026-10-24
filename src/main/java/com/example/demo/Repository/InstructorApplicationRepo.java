package com.example.demo.Repository;

import com.example.demo.Model.InstructorApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorApplicationRepo extends JpaRepository<InstructorApplication,Long> {
}

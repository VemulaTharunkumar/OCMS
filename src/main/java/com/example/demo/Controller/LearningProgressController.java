package com.example.demo.Controller;

import com.example.demo.Model.CourseContent;
import com.example.demo.Service.LearningProgressService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/learning-progress")
public class LearningProgressController {

    private final LearningProgressService lservice;

    public LearningProgressController(LearningProgressService lservice) {
        this.lservice = lservice;
    }

    @PutMapping("/complete")
    public ResponseEntity<CourseContent> tickbyContent(
            @RequestParam long enrollmentid,@RequestParam long contentid) {

        CourseContent content = lservice.tickbyContent(enrollmentid,contentid).getContent();
        return ResponseEntity.ok(content);
    }
}

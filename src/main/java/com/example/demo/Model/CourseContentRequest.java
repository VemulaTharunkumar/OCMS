package com.example.demo.Model;

import com.example.demo.Enumeration.ContentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseContentRequest {
    private ContentType contenttype;
    private String contenturl;
    private Integer sequenceOrder;
}
package com.project.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.services.CommentServices;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    private CommentServices commentServices;

}

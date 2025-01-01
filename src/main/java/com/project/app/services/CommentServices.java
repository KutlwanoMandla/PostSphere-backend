package com.project.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.repositories.CommentRepo;

@Service
public class CommentServices {
    

    @Autowired
    private CommentRepo commentRepo;
}

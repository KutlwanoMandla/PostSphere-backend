package com.project.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.entities.Like;
import com.project.app.services.LikeServices;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    

    @Autowired
    private LikeServices likeServices;

}

package com.project.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.repositories.LikeRepo;

@Service
public class LikeServices {
    
    @Autowired
    private LikeRepo likeRepo;
}

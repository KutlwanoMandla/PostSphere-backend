package com.project.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.project.app.entities.Comment;

@RepositoryRestResource
public interface CommentRepo extends JpaRepository<Comment, Long>{
    
}

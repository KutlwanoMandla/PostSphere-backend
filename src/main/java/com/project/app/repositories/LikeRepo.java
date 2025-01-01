package com.project.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.project.app.entities.Like;

@RepositoryRestResource
public interface LikeRepo extends JpaRepository<Like, Long>{
    
}

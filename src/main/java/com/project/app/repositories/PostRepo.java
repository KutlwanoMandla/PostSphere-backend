package com.project.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.project.app.entities.Post;

@RepositoryRestResource
public interface PostRepo extends JpaRepository<Post, Long>{

    List<Post> findAllByAuthorId(Long authorId);
    
    // Optional<Post> findByAuthor(User author);
}

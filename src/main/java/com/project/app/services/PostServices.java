package com.project.app.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.entities.Comment;
import com.project.app.entities.Like;
import com.project.app.entities.Post;
import com.project.app.entities.User;
import com.project.app.imageStorage.ImageService;
import com.project.app.repositories.CommentRepo;
import com.project.app.repositories.LikeRepo;
import com.project.app.repositories.PostRepo;
import com.project.app.repositories.UserRepo;

@Service
@Transactional
public class PostServices {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserServices userServices;

    public Post createPost(String title, String intro, String content, MultipartFile thumbnail, User author)
            throws IOException {

        Post post = new Post();
        post.setTitle(title);
        post.setIntro(intro);
        post.setContent(content);
        post.setCreated_at(LocalDateTime.now());
        post.setAuthor(author);

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String imageUrl = imageService.saveImage(thumbnail);
            post.setThumbnail(imageUrl);
        }

        return postRepo.save(post);
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long postId) {
        return postRepo.findById(postId);
    }

    public Comment addComment(Long postId, String username, String content) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        post.getComments().add(comment);
        postRepo.save(post);

        return commentRepo.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepo.findById(commentId);
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("Comment does not belong to the specified post");
        }

        commentRepo.delete(comment);
    }

    public boolean toggleLike(Long postId, String username) {
        Post post = getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userServices.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Like> existingLike = post.getLikes().stream()
                .filter(like -> like.getUser().getUsername().equals(username))
                .findFirst();

        if (existingLike.isPresent()) {
            Like likeToRemove = existingLike.get();
            post.getLikes().remove(likeToRemove);
            
            
            likeRepo.delete(likeToRemove);
            
            postRepo.save(post);
            return false;
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            like.setCreatedAt(LocalDateTime.now());
            if (post.getLikes() == null) {
                post.setLikes(new ArrayList<>());
            }
            post.getLikes().add(like);
            postRepo.save(post);
            return true;
        }
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        // Delete the thumbnail from Cloudinary if it exists
        if (post.getThumbnail() != null && !post.getThumbnail().isEmpty()) {
            imageService.deleteImage(post.getThumbnail());
        }
        
        postRepo.delete(post);
    }

}
package com.project.app.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.app.DTO.CommentDTO;
import com.project.app.DTO.PostDTO;
import com.project.app.DTO.UserDTO;
import com.project.app.entities.Comment;
import com.project.app.entities.Post;
import com.project.app.entities.User;
import com.project.app.services.PostServices;
import com.project.app.services.UserServices;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "https://post-sphere-app.onrender.com")
public class PostController {

    @Autowired
    private PostServices postServices;

    @Autowired
    private UserServices userServices;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @RequestParam("title") String title,
            @RequestParam("intro") String intro,
            @RequestParam("content") String content,
            @RequestParam("authorId") Long authorId,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws java.io.IOException {
        try {
            User author = userServices.getUserById(authorId).orElseThrow(() -> new RuntimeException("User not found"));

            Post post = postServices.createPost(title, intro, content, thumbnail, author);
            return ResponseEntity.ok("Post created with ID: " + post.getId());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to create post: " + e.getMessage());
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        Post post = postServices.getPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        PostDTO postDTO = convertToPostDTO(post);

        return ResponseEntity.ok(postDTO);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> addCommentToPost(
            @PathVariable Long postId,
            @RequestParam("username") String username,
            @RequestParam("content") String content) {

        Comment comment = postServices.addComment(postId, username, content);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());

        User user = userServices.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        UserDTO userDTO = convertToUserDTO(user);

        commentDTO.setUser(userDTO);

        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> posts = postServices.getAllPosts();

        List<PostDTO> postDTOs = posts.stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam("username") String username) {

        Comment comment = postServices.getCommentById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(403).body("You are not authorized to delete this comment");
        }

        postServices.deleteComment(postId, commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<String> toggleLike(
            @PathVariable Long postId,
            @RequestParam("username") String username) {
        try {
            boolean isLiked = postServices.toggleLike(postId, username);
            String message = isLiked ? "Post liked successfully" : "Post unliked successfully";
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postServices.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }

    // private methods

    private PostDTO convertToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setIntro(post.getIntro());
        postDTO.setContent(post.getContent());
        postDTO.setTitle(post.getTitle());
        postDTO.setThumbnailUrl(post.getThumbnail());
        postDTO.setCreatedAt(post.getCreated_at());
        postDTO.setLikeCount(post.getLikes().size());

        postDTO.setComments(post.getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList()));

        UserDTO authorDTO = new UserDTO();
        authorDTO.setId(post.getAuthor().getId());
        authorDTO.setUsername(post.getAuthor().getUsername());
        authorDTO.setEmail(post.getAuthor().getEmail());

        postDTO.setAuthor(authorDTO);

        return postDTO;
    }

    private CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUser(convertToUserDTO(comment.getUser()));
        return dto;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

}

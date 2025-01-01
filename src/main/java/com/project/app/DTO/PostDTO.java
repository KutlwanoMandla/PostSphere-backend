package com.project.app.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String intro;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private UserDTO author;
    private int likeCount;
    private List<CommentDTO> comments;
}
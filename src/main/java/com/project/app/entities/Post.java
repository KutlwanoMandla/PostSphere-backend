package com.project.app.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "intro", nullable = false, length = 500)
    private String intro;

    // @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    // @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post") // cascade = CascadeType.ALL is giving me a problem. It saves every comment twice.
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes;

    public Post() {}

    public Post(Long id, String title, String intro, String content, LocalDateTime created_at, String thumbnail, User author, List<Comment> comments, List<Like> likes) {
        this.id = id;
        this.title = title;
        this.intro = intro;
        this.content = content;
        this.created_at = created_at;
        this.thumbnail = thumbnail;
        this.author = author;
        this.comments = comments;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}

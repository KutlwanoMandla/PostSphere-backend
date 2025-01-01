package com.project.app.Authentication.dto;

public class AuthenticationResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String password;
    private String token;

    // Constructor
    public AuthenticationResponse(Long id, String username, String email, String bio, String password, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.password = password;
        this.token = token;
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for bio
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

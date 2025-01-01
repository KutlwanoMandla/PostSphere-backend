package com.project.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.app.entities.User;
import com.project.app.services.UserServices;



@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://post-sphere-app.onrender.com")
public class UserController {
    
    @Autowired
    private UserServices userServices;

    @PostMapping
    public void createUser(@RequestBody User user){
        userServices.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userServices.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userServices.getUserById(userId).orElse(null);
    }

    @PutMapping("/{userId}/bio")
    public User updateUserBio(@PathVariable Long userId, @RequestBody String bio) {
        return userServices.updateUserBio(bio, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userServices.deleteUserById(userId);
    }
    
}

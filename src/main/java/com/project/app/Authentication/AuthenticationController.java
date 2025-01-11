package com.project.app.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.Authentication.dto.AuthenticationRequest;
import com.project.app.Authentication.dto.AuthenticationResponse;
import com.project.app.Authentication.dto.SignupRequest;
import com.project.app.entities.User;
import com.project.app.repositories.UserRepo;
import com.project.app.services.UserServices;
import com.project.app.services.VerificationCodeServices;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserRepo userRepo;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserServices userServices;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeServices verificationCodeServices;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService,
            UserServices userServices,
            VerificationCodeServices verificationCodeServices,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userServices = userServices;
        this.verificationCodeServices = verificationCodeServices;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        // Check if username already exists
        if (userServices.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Check if email already exists
        if (userServices.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // Create new user
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getBio(),
                passwordEncoder.encode(request.getPassword()));

        userServices.createUser(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(new HashMap<>(), userDetails);

        Optional<User> userObj = userServices.findByUsername(request.getUsername());

        User user = userObj.get();
        AuthenticationResponse response = new AuthenticationResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getPassword(),
                token);
        return ResponseEntity.ok(response);
    }

    // reset password functions
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // Check if the email exists
        if (userServices.findByEmail(email).isEmpty()) {
            return ResponseEntity.badRequest().body("Email not found");
        }

        // Generate and send verification code
        String code = verificationCodeServices.generateCode(email);
        
        return ResponseEntity.ok("Verification code sent to your email: " + code);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("verificationCode");
        String newPassword = request.get("newPassword");

        // Validate the verification code
        if (!verificationCodeServices.validateCode(email, code)) {
            return ResponseEntity.badRequest().body("Invalid or expired verification code");
        }

        // Update the user's password
        Optional<User> userOptional = userServices.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
        }

        return ResponseEntity.ok("Password reset successfully");
    }

}

package com.slackclone.auth_service.service;

import com.slackclone.auth_service.dto.AuthResponse;
import com.slackclone.auth_service.dto.LoginRequest;
import com.slackclone.auth_service.dto.RegisterRequest;
import com.slackclone.auth_service.model.User;
import com.slackclone.auth_service.repository.UserRepository;
import com.slackclone.auth_service.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username already exists");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );
        userRepository.save(user);

        String token = jwtUtil.generateToken
                       (user.getUsername(),
                        user.getId());
        return new AuthResponse(
                   token,
                   user.getUsername());                     
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(
                       user.getUsername(),
                       user.getId());
        return new AuthResponse(
                   token, 
                   user.getUsername());
    }
}

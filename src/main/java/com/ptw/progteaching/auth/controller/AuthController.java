package com.ptw.progteaching.auth.controller;

import com.ptw.progteaching.auth.entity.User;
import com.ptw.progteaching.auth.model.RegisterRequest;
import com.ptw.progteaching.repository.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // TODO
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
//        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
//            return Map.of("error", "Username already exists");
//        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail("Something");
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    // TODO
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(
            @RequestBody RegisterRequest req,
            HttpSession session) {

        User user = userRepository.findByUsername(req.getUsername())
                .orElse(null);
//
//        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
//            return Map.of("error", "Invalid credentials");
//        }

//        // Generate tokens (hidden from frontend)
//        String accessToken = tokenManager.generateAccessToken(user.getUsername(), user.getId());
//        String refreshToken = tokenManager.generateRefreshToken(user.getUsername(), user.getId());
//
//        // Store in session (Spring handles JSESSIONID automatically)
//        SessionTokens tokens = new SessionTokens(accessToken, refreshToken, user.getId(), user.getUsername());
//        session.setAttribute("tokens", tokens);

        return ResponseEntity.ok(passwordEncoder.matches(req.getPassword(), user.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.ok().build();
    }
}

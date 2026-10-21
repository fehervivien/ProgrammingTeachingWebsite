package com.ptw.progteaching.auth.controller;

import com.ptw.progteaching.auth.model.request.LoginRequest;
import com.ptw.progteaching.auth.model.request.RegisterRequest;
import com.ptw.progteaching.auth.model.response.UserInfo;
import com.ptw.progteaching.auth.service.AuthService;
import com.ptw.progteaching.common.service.SessionService;
import com.ptw.progteaching.common.validation.ValidationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

// TODO Frontend esetén alakítani
@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final ValidationService validationService;
    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {

        validationService.badRequestFieldValidations(request);

        Authentication authentication = authService.register(request);

        sessionService.startSession(authentication);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {

        validationService.badRequestFieldValidations(request);

        Authentication authentication = authService.login(request);

        sessionService.startSession(authentication);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> me(@AuthenticationPrincipal UserInfo userInfo) {
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        sessionService.invalidateSession(session);

        return ResponseEntity.ok().build();
    }

    // TODO csak tesztelésnek van itt törölni később
    @GetMapping("/debug/session")
    public ResponseEntity<Map<String, Object>> debugSession(HttpSession session) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", session.getId());
        m.put("created", Instant.ofEpochMilli(session.getCreationTime()));
        m.put("lastAccessed", Instant.ofEpochMilli(session.getLastAccessedTime()));
        m.put("maxInactiveSeconds", session.getMaxInactiveInterval());
        return ResponseEntity.ok(m);
    }
}

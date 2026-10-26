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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final ValidationService validationService;
    private final AuthService authService;
    private final SessionService sessionService;


    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request) {
        try {
            validationService.badRequestFieldValidations(request);
            Authentication authentication = authService.register(request);
            sessionService.startSession(authentication);

            return "redirect:/";
        } catch (Exception e) {
            log.error("Regisztrációs hiba", e);
            return "redirect:/register?error";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request) {
        try {
            validationService.badRequestFieldValidations(request);
            Authentication authentication = authService.login(request);
            sessionService.startSession(authentication);

            return "redirect:/";
        } catch (Exception e) {
            log.error("Bejelentkezési hiba", e);
            return "redirect:/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        sessionService.invalidateSession(session);
        return "redirect:/login?logout";
    }


    @GetMapping("/api/auth/me")
    @ResponseBody
    public UserInfo me(@AuthenticationPrincipal UserInfo userInfo) {
        return userInfo;
    }

    // TODO csak tesztelésnek van itt törölni később
    @GetMapping("/api/auth/debug/session")
    @ResponseBody
    public Map<String, Object> debugSession(HttpSession session) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", session.getId());
        m.put("created", Instant.ofEpochMilli(session.getCreationTime()));
        m.put("lastAccessed", Instant.ofEpochMilli(session.getLastAccessedTime()));
        m.put("maxInactiveSeconds", session.getMaxInactiveInterval());
        return m;
    }
}
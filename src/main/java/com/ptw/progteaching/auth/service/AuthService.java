package com.ptw.progteaching.auth.service;

import com.ptw.progteaching.auth.model.exception.InvalidCredentialsException;
import com.ptw.progteaching.common.entity.User;
import com.ptw.progteaching.auth.mapper.UserMapper;
import com.ptw.progteaching.auth.model.request.LoginRequest;
import com.ptw.progteaching.auth.model.request.RegisterRequest;
import com.ptw.progteaching.auth.validation.AuthValidationService;
import com.ptw.progteaching.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthValidationService authValidationService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Authentication register(RegisterRequest registerRequest) {
        authValidationService.validateRegister(registerRequest);

        User user = userMapper.mapRegisterRequestToUser(registerRequest);
        userService.saveUser(user);

        return authenticate(registerRequest.getUsername());
    }

    public Authentication login(LoginRequest loginRequest) {
        authValidationService.validateLogin(loginRequest);

        User user = userService.findByUsername(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return authenticate(loginRequest.getUsername());
    }

    private Authentication authenticate(String username) {
        UserDetails userInfo = userService.loadUserByUsername(username);

        return UsernamePasswordAuthenticationToken.authenticated(userInfo, null, userInfo.getAuthorities());
    }
}

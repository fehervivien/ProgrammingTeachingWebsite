package com.ptw.progteaching.auth.validation;

import com.ptw.progteaching.auth.model.exception.UserAlreadyExistException;
import com.ptw.progteaching.auth.model.exception.InvalidCredentialsException;
import com.ptw.progteaching.auth.model.request.LoginRequest;
import com.ptw.progteaching.auth.model.request.RegisterRequest;
import com.ptw.progteaching.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthValidationService {

    private final UserService userService;

    public void validateRegister(RegisterRequest request) {
        if (userService.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new UserAlreadyExistException();
        }
    }

    public void validateLogin(LoginRequest request) {
        String username = request.getUsername();

        if (!userService.existsByUsername(username)) {
            throw new InvalidCredentialsException();
        }
    }
}

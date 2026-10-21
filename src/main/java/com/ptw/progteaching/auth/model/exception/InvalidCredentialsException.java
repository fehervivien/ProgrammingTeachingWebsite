package com.ptw.progteaching.auth.model.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class InvalidCredentialsException extends UsernameNotFoundException {

    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}

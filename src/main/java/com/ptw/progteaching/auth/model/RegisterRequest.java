package com.ptw.progteaching.auth.model;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
}

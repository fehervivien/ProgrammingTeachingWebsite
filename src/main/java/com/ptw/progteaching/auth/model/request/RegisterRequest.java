package com.ptw.progteaching.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    private String username;

    @ToString.Exclude
    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;
}

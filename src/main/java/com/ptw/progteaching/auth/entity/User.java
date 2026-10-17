package com.ptw.progteaching.auth.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Data
public class User {

    @Id
    private Long id;
    private String username;
    private String email;

    @ToString.Exclude
    private String password;
}

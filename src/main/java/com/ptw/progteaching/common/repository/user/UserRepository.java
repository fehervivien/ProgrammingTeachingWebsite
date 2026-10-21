package com.ptw.progteaching.common.repository.user;

import com.ptw.progteaching.common.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
}

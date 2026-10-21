package com.ptw.progteaching.common.service;

import com.ptw.progteaching.auth.model.exception.InvalidCredentialsException;
import com.ptw.progteaching.auth.model.response.UserInfo;
import com.ptw.progteaching.common.entity.User;
import com.ptw.progteaching.common.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
    }

    // TODO dupla load, cachelni ha már egyszer beloadoltuk
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);

        return new UserInfo(user.getUsername(), user.getEmail());
    }
}

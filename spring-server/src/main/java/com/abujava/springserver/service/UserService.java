package com.abujava.springserver.service;

import com.abujava.springserver.exception.RestException;
import com.abujava.springserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class is not documented :(
 *
 * @author Muhammad Muminov
 * @since 7/9/2023
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with username %s", username)));
    }

    public UserDetails loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> RestException.notFound("User"));

    }
}

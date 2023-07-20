package com.abujava.springserver.controller;

import com.abujava.springserver.exception.RestException;
import com.abujava.springserver.model.User;
import com.abujava.springserver.repository.UserRepository;
import com.abujava.springserver.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/me")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public User getCurrentUser(@CurrentUser User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> RestException.notFound("User"));
    }
}

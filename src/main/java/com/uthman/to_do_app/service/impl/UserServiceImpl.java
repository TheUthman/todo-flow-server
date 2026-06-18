package com.uthman.to_do_app.service.impl;

import com.uthman.to_do_app.dto.user.ChangePasswordRequest;
import com.uthman.to_do_app.dto.user.UserResponse;
import com.uthman.to_do_app.dto.user.UserUpdateRequest;
import com.uthman.to_do_app.entity.User;
import com.uthman.to_do_app.exception.BadRequestException;
import com.uthman.to_do_app.exception.ResourceNotFoundException;
import com.uthman.to_do_app.repository.UserRepository;
import com.uthman.to_do_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getCurrentUser() {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Long id = Long.parseLong(idStr);
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } catch (NumberFormatException e) {
            return userRepository.findByUsername(idStr)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public UserResponse getProfile() {
        return mapToResponse(getCurrentUser());
    }

    @Override
    public UserResponse updateProfile(UserUpdateRequest request) {
        User user = getCurrentUser();

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("Username already taken");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already taken");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

package com.uthman.to_do_app.service;

import com.uthman.to_do_app.dto.user.ChangePasswordRequest;
import com.uthman.to_do_app.dto.user.UserResponse;
import com.uthman.to_do_app.dto.user.UserUpdateRequest;

public interface UserService {
    UserResponse getProfile();
    UserResponse updateProfile(UserUpdateRequest request);
    void changePassword(ChangePasswordRequest request);
}

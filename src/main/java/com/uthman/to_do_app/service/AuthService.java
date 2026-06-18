package com.uthman.to_do_app.service;

import com.uthman.to_do_app.dto.auth.AuthResponse;
import com.uthman.to_do_app.dto.auth.LoginRequest;
import com.uthman.to_do_app.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}
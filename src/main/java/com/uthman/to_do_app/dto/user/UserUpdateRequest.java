package com.uthman.to_do_app.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String password;
}

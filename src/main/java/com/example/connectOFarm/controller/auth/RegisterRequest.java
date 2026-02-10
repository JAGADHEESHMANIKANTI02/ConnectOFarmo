package com.example.connectOFarm.controller.auth;

import com.example.connectOFarm.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String location;
    private Role role; // Optional, defaults to USER if null logic handled in service
}

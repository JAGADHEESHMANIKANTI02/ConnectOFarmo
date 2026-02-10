package com.example.connectOFarm.service;

import com.example.connectOFarm.controller.auth.AuthenticationRequest;
import com.example.connectOFarm.controller.auth.AuthenticationResponse;
import com.example.connectOFarm.controller.auth.RegisterRequest;
import com.example.connectOFarm.models.Role;
import com.example.connectOFarm.models.User;
import com.example.connectOFarm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var user = User.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole() != null ? request.getRole() : Role.CONSUMER)
                                .phone(request.getPhone())
                                .location(request.getLocation())
                                .createdAt(new Date())
                                .build();
                repository.save(user);

                // Add claims like role and userId
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("role", user.getRole());
                extraClaims.put("userId", user.getId());

                var jwtToken = jwtService.generateToken(extraClaims, user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();

                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("role", user.getRole());
                extraClaims.put("userId", user.getId());

                var jwtToken = jwtService.generateToken(extraClaims, user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }
}

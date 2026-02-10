package com.example.connectOFarm.service;

import com.example.connectOFarm.dto.UserDto;
import com.example.connectOFarm.models.Role;
import com.example.connectOFarm.models.User;
import com.example.connectOFarm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    public List<UserDto> getFarmers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.FARMER)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getConsumers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.CONSUMER)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .location(user.getLocation())
                .role(user.getRole())
                .build();
    }
}

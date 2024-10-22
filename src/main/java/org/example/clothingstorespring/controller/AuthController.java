package org.example.clothingstorespring.controller;

import org.example.clothingstorespring.constants.ApiConstants;
import org.example.clothingstorespring.dto.ApiResponseDTO;
import org.example.clothingstorespring.model.User;
import org.example.clothingstorespring.model.Role;
import org.example.clothingstorespring.repository.RoleRepository;
import org.example.clothingstorespring.repository.UserRepository;
import org.example.clothingstorespring.dto.RegistrationRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> registerUser(@RequestBody RegistrationRequestDTO registrationRequestDTO) {
        Optional<User> existingUser = userRepository.findByUsername(registrationRequestDTO.getUsername());
        if (existingUser.isPresent()) {
            ApiResponseDTO response = new ApiResponseDTO(false, "Already exist", ApiConstants.USER_EXISTS);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registrationRequestDTO.getUsername());
        user.setEmail(registrationRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequestDTO.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        userRepository.save(user);


        ApiResponseDTO response = new ApiResponseDTO(true, "success", ApiConstants.USER_CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);    }
}
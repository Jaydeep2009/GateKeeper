package com.gatekeeper.gatekeeper.service;

import com.gatekeeper.gatekeeper.dto.UserRequest;
import com.gatekeeper.gatekeeper.model.User;
import com.gatekeeper.gatekeeper.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRequest request) {

        //check user already exists
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        //hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user= new User();
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User loginUser(UserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }


}

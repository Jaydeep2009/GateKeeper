package com.gatekeeper.gatekeeper.controller;

import com.gatekeeper.gatekeeper.dto.UserRequest;
import com.gatekeeper.gatekeeper.model.User;
import com.gatekeeper.gatekeeper.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    //Register API
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest request) {
        User user = userService.registerUser(request);

        return ResponseEntity.status(201).body(user);
    }


    // 🔹 Login API
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserRequest request) {

        User user = userService.loginUser(request);

        return ResponseEntity.ok(user);
    }


}

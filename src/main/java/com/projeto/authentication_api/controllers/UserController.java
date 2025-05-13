package com.projeto.authentication_api.controllers;

import com.projeto.authentication_api.dtos.LoginDto;
import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.services.LoginService;
import com.projeto.authentication_api.services.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegisterService registerService;
    private final LoginService loginService;

    public UserController(RegisterService registerService, LoginService loginService) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        UserDto created = registerService.register(userDto);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        String result = loginService.login(loginDto, request);
        return ResponseEntity.ok(result);
    }
}




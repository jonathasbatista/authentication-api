package com.projeto.authentication_api.controllers;

import com.projeto.authentication_api.dtos.LoginDto;
import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto) {
        logger.info("Requisição para registrar usuário: {}", userDto.username());
        return userService.register(userDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        logger.info("Tentativa de login para o usuário: {}", loginDto.username());
        return userService.login(loginDto, request);
    }
}



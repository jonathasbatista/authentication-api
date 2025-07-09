package com.projeto.authentication_api.controllers;

import com.projeto.authentication_api.dtos.LoginDto;
import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.models.UserModel;
import com.projeto.authentication_api.services.JWTService;
import com.projeto.authentication_api.services.LoginService;
import com.projeto.authentication_api.services.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<?> register(@RequestBody UserDto userDto, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        UserDto user = registerService.register(userDto, ip);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        try {
            UserModel user = loginService.login(loginDto, request);
            if (user == null) {
                return ResponseEntity.status(202).body("Código de verificação enviado para o e-mail.");
            }
            String token = JWTService.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}




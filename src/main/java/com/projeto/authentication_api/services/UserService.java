package com.projeto.authentication_api.services;

import com.projeto.authentication_api.dtos.LoginDto;
import com.projeto.authentication_api.dtos.UserDto;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserDto register(UserDto userDto);
    String login(LoginDto loginDto, HttpServletRequest request);
}

package com.projeto.authentication_api.dtos;

public record LoginDto (
        String username,
        String password,
        String captcha,
        String code
){}

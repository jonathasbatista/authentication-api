package com.projeto.authentication_api.dtos;

//Determina o que é esperado do usuário para que seja salvo no banco de dados
public record UserDto (
        String username,
        String password,
        String name,
        String email,
        String profile,
        String ipAuthorized
){}

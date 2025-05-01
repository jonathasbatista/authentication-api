package com.projeto.authentication_api.dtos;

import com.projeto.authentication_api.enums.ProfileEnum;

//Determina o que é esperado do usuário para que seja salvo no banco de dados
public record UserDto (
        String username,
        String password,
        String name,
        String email,
        ProfileEnum profile
){}

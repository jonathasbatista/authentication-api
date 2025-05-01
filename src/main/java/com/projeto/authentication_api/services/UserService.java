package com.projeto.authentication_api.services;

import com.projeto.authentication_api.dtos.UserDto;

public interface UserService {

    public UserDto save(UserDto userDto);

    UserDto getByUsername(String username);

    UserDto update(String username, UserDto userDto);

    void delete(String username);
}

package com.projeto.authentication_api.controllers;

import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//Responsável por receber requisições HTTP, processar e devolver uma resposta
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    private UserDto save(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }

    @GetMapping("/{username}")
    public UserDto getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @PutMapping("/{username}")
    private UserDto update(@PathVariable String username, @RequestBody UserDto userDto) {
        return userService.update(username, userDto);
    }

    @DeleteMapping("/{username}")
    private void delete(@PathVariable String username) {
        userService.delete(username);
    }
}

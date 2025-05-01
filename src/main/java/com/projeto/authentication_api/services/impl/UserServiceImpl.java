package com.projeto.authentication_api.services.impl;

import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.models.UserModel;
import com.projeto.authentication_api.repositories.UserRepository;
import com.projeto.authentication_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Responsável por regras de negócios
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto save(UserDto userDto) {

        UserModel userAlreadyExists = userRepository.findByUsername(userDto.username());
        UserModel emailAlreadyExists = userRepository.findByEmail(userDto.email());
        if(userAlreadyExists != null){
            throw new RuntimeException("Usuário já existe.");
        } else if (emailAlreadyExists != null) {
            throw new RuntimeException("Email já está vinculado a outra conta.");
        }

        var passwordHash = passwordEncoder.encode(userDto.password());

        UserModel entity = new UserModel(userDto.username(), passwordHash, userDto.name(), userDto.email(), userDto.profile());

        UserModel newUser = userRepository.save(entity);

        return new UserDto(newUser.getUsername(), newUser.getPassword(), newUser.getName(), newUser.getEmail(), newUser.getProfile());
    }

    @Override
    public UserDto getByUsername(String username) {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        else {
            return new UserDto(
                    user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getEmail(),
                    user.getProfile()
            );
        }
    }

    @Override
    public UserDto update(String username, UserDto userDto) {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        else {
            if (userDto.name() != null) {
                user.setName(userDto.name());
            }
            if (userDto.email() != null) {
                user.setEmail(userDto.email());
            }
            if (userDto.password() != null) {
                user.setPassword(passwordEncoder.encode(userDto.password()));
            }
            if (userDto.email() != null) {
                user.setEmail(userDto.email());
            }
            if (userDto.profile() != null) {
                user.setProfile(userDto.profile());
            }
        }
        UserModel updatedUser = userRepository.save(user);

        return new UserDto(updatedUser.getUsername(), updatedUser.getPassword(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getProfile());
    }

    @Override
    public void delete(String username) {
        UserModel user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        else {
            userRepository.delete(user);
        }
    }

}

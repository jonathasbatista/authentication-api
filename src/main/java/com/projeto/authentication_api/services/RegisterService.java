package com.projeto.authentication_api.services;

import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.exceptions.ValidationException;
import com.projeto.authentication_api.models.UserModel;
import com.projeto.authentication_api.repositories.UserRepository;
import com.projeto.authentication_api.utils.PasswordUtil;
import com.projeto.authentication_api.utils.SanitizerUtil;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto register(UserDto dto, String ip) {

        String username = SanitizerUtil.sanitizeUsername(dto.username());
        String password = SanitizerUtil.sanitizePassword(dto.password());
        String name = SanitizerUtil.sanitizeName(dto.name());
        String email = SanitizerUtil.sanitizeEmail(dto.email());
        String profile = SanitizerUtil.sanitizeProfile(dto.profile());

        if (userRepository.findByUsername(username) != null) {
            throw new ValidationException("Usuário já existe.");
        } else if (userRepository.findByEmail(email) != null) {
            throw new ValidationException("Email já está vinculado a outra conta.");
        }

        String passwordHash = PasswordUtil.hashPassword(dto.password().trim());

        UserModel user = new UserModel(username, passwordHash, name, email, profile, ip);

        UserModel saved = userRepository.save(user);

        return new UserDto(saved.getUsername(), null, saved.getName(), saved.getEmail(), saved.getProfile(), saved.getIpAuthorized());
    }
}

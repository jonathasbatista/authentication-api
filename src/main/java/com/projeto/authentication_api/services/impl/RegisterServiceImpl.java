package com.projeto.authentication_api.services;

import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.exceptions.ValidationException;
import com.projeto.authentication_api.models.UserModel;
import com.projeto.authentication_api.repositories.UserRepository;
import com.projeto.authentication_api.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private static final String EMAIL_REGEX = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";

    private final UserRepository userRepository;

    public RegisterServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto register(UserDto dto) {
        logger.info("Requisição para cadastrar novo usuário: {}", dto.username());

        String username = dto.username().trim();
        String email = dto.email().trim();

        if (userRepository.findByUsername(username) != null) {
            throw new ValidationException("Usuário já existe.");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new ValidationException("Email já está vinculado a outra conta.");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException("Email inválido.");
        }

        String passwordHash = PasswordUtil.hashPassword(dto.password().trim());

        UserModel user = new UserModel(username, passwordHash, dto.name().trim(), email, dto.profile().trim(), dto.ipAuthorized());

        UserModel saved = userRepository.save(user);
        logger.info("Usuário {} cadastrado com sucesso", saved.getUsername());

        return new UserDto(saved.getUsername(), null, saved.getName(), saved.getEmail(), saved.getProfile(), saved.getIpAuthorized());
    }
}

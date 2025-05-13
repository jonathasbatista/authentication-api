package com.projeto.authentication_api.services.impl;

import com.projeto.authentication_api.exceptions.AuthenticationException;
import com.projeto.authentication_api.exceptions.ValidationException;
import com.projeto.authentication_api.dtos.LoginDto;
import com.projeto.authentication_api.dtos.UserDto;
import com.projeto.authentication_api.models.UserModel;
import com.projeto.authentication_api.repositories.UserRepository;
import com.projeto.authentication_api.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.projeto.authentication_api.utils.PasswordUtil.hashPassword;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String EMAIL_REGEX = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    private static final ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> captchaStore = new ConcurrentHashMap<>();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
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

        String passwordHash = hashPassword(dto.password().trim());

        UserModel user = new UserModel(username, passwordHash, dto.name().trim(), email, dto.profile().trim(), dto.ipAuthorized());
        UserModel saved = userRepository.save(user);

        logger.info("Usuário {} cadastrado com sucesso", saved.getUsername());
        return new UserDto(saved.getUsername(), null, saved.getName(), saved.getEmail(), saved.getProfile(), saved.getIpAuthorized());
    }

    @Override
    public String login(LoginDto loginDto, HttpServletRequest request) {
        String username = loginDto.username().trim();
        String password = loginDto.password().trim();
        String captchaInput = loginDto.captcha();

        UserModel user = userRepository.findByUsername(username);
        if (user == null || !hashPassword(password).equals(user.getPassword())) {
            return handleLoginFailure(username, captchaInput);
        }

        if (!request.getRemoteAddr().equals(user.getIpAuthorized())) {
            throw new AuthenticationException("IP não autorizado");
        }

        loginAttempts.remove(username);
        captchaStore.remove(username);
        return "Login realizado com sucesso";
    }

    private String handleLoginFailure(String username, String captchaInput) {
        int attempts = loginAttempts.merge(username, 1, Integer::sum);

        if (attempts >= 1) {
            String generatedCaptcha = captchaStore.computeIfAbsent(username, k -> generateCaptcha());

            if (captchaInput == null || !captchaInput.equals(generatedCaptcha)) {
                throw new AuthenticationException("CAPTCHA necessário ou inválido: " + generatedCaptcha);
            }

            captchaStore.remove(username);
        }

        throw new AuthenticationException("Usuário ou senha inválidos");
    }

    private String generateCaptcha() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}

package com.projeto.authentication_api.services.impl;

import com.projeto.authentication_api.exceptions.AuthenticationException;
import com.projeto.authentication_api.models.UserModel;
import com.projeto.authentication_api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.projeto.authentication_api.utils.PasswordUtil.hashPassword;

@Service
public class LoginServiceImpl {
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();

    public LoginServiceImpl(UserRepository userRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public UserModel validateUserAndPassword(String username, String password) {
        UserModel user = userRepository.findByUsername(username);
        if (user == null || !hashPassword(password).equals(user.getPassword())) {
            incrementAttempts(username);
            throw new AuthenticationException("Usuário ou senha inválidos");
        }
        return user;
    }

    public void validateIp(UserModel user, String ip) {
        if (!ip.equals(user.getIpAuthorized())) {
            throw new AuthenticationException("IP não autorizado");
        }
    }

    public void handle2FA(String username, String email, String inputCode) {
        if (!verificationCodes.containsKey(username)) {
            String code = String.format("%06d", new Random().nextInt(999999));
            verificationCodes.put(username, code);
            emailService.sendVerificationCode(email, code);
            throw new AuthenticationException("Código de verificação enviado para o e-mail");
        }

        if (inputCode == null || !inputCode.equals(verificationCodes.get(username))) {
            throw new AuthenticationException("Código de verificação inválido");
        }

        verificationCodes.remove(username);
        loginAttempts.remove(username);
    }

    public void incrementAttempts(String username) {
        loginAttempts.merge(username, 1, Integer::sum);
    }

    public int getAttempts(String username) {
        return loginAttempts.getOrDefault(username, 0);
    }

    public void clearAttempts(String username) {
        loginAttempts.remove(username);
    }
}


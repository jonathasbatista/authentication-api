package com.projeto.authentication_api.services;

import com.projeto.authentication_api.dtos.LoginDto;
import com.projeto.authentication_api.exceptions.AuthenticationException;
import com.projeto.authentication_api.exceptions.ValidationException;
import com.projeto.authentication_api.models.UserModel;
import com.projeto.authentication_api.repositories.UserRepository;
import com.projeto.authentication_api.utils.SanitizerUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.projeto.authentication_api.utils.PasswordUtil.hashPassword;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CaptchaService captchaService;
    private final ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();

    public LoginService(UserRepository userRepository, EmailService emailService, CaptchaService captchaService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.captchaService = captchaService;
    }

    public UserModel login(LoginDto loginDto, HttpServletRequest request) {
        String username = SanitizerUtil.sanitizeUsername(loginDto.username());
        String password = SanitizerUtil.sanitizePassword(loginDto.password());
        String code = SanitizerUtil.sanitizeCode(loginDto.code());
        String captcha = SanitizerUtil.sanitizeCaptcha(loginDto.captcha());

        if (username == null || password == null) {
            throw new ValidationException("Usuário e senha são obrigatórios.");
        }

        if (getAttempts(username) >= 2) {
            captchaService.generateOrGetCaptcha(username);
            captchaService.validateCaptcha(username, captcha);
        }

        UserModel user = validateUserAndPassword(username, password);
        validateIp(user, request.getRemoteAddr());

        // Se ainda não tiver enviado o código de verificação, envia e lança exceção
        if (!verificationCodes.containsKey(username)) {
            String generatedCode = String.format("%06d", new Random().nextInt(999999));
            verificationCodes.put(username, generatedCode);
            emailService.sendVerificationCode(user.getEmail(), generatedCode);
            String message = "Código de verificação enviado para o e-mail.";
            return null;
        }

        if (code == null || !code.equals(verificationCodes.get(username))) {
            throw new AuthenticationException("Código de verificação inválido ou expirado.");
        }

        verificationCodes.remove(username);
        clearAttempts(username);
        captchaService.clearCaptcha(username);
        return user;
    }

    private UserModel validateUserAndPassword(String username, String password) {
        UserModel user = userRepository.findByUsername(username);
        if (user == null || !hashPassword(password).equals(user.getPassword())) {
            incrementAttempts(username);
            throw new AuthenticationException("Usuário ou senha inválidos.");
        }
        return user;
    }

    private void validateIp(UserModel user, String ip) {
        if (!ip.equals(user.getIpAuthorized())) {
            throw new AuthenticationException("IP não autorizado.");
        }
    }

    private String handle2FA(String username, String email, String inputCode) {
        if (!verificationCodes.containsKey(username)) {
            String code = String.format("%06d", new Random().nextInt(999999));
            verificationCodes.put(username, code);
            emailService.sendVerificationCode(email, code);
            return "Código de verificação enviado para o e-mail.";
        }

        if (inputCode == null || !inputCode.equals(verificationCodes.get(username))) {
            throw new AuthenticationException("Código de verificação inválido ou expirado.");
        }
        verificationCodes.remove(username);
        return "Código válidado";
    }

    private void incrementAttempts(String username) {
        loginAttempts.merge(username, 1, Integer::sum);
    }

    private int getAttempts(String username) {
        return loginAttempts.getOrDefault(username, 0);
    }

    private void clearAttempts(String username) {
        loginAttempts.remove(username);
    }
}

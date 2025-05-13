package com.projeto.authentication_api.services;

import com.projeto.authentication_api.exceptions.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {
    private final ConcurrentHashMap<String, String> captchaStore = new ConcurrentHashMap<>();

    public String generateOrGetCaptcha(String username) {
        String captcha = captchaStore.computeIfAbsent(username, k -> String.format("%06d", new Random().nextInt(999999)));
        return captcha;
    }

    public void validateCaptcha(String username, String input) {
        String expected = captchaStore.get(username);
        if (expected == null || !expected.equals(input)) {
            throw new AuthenticationException("CAPTCHA necessário ou inválido: " + expected);
        }
        captchaStore.remove(username);
    }

    public void clearCaptcha(String username) {
        captchaStore.remove(username);
    }
}


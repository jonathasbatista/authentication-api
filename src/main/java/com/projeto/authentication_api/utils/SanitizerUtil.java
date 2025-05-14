package com.projeto.authentication_api.utils;

public class SanitizerUtil {
    public static String sanitizeUsername(String input) {
        return input == null ? null : input.trim().replaceAll("[^a-zA-Z0-9_.-]", "");
    }

    public static String sanitizePassword(String input) {
        if (input == null || input.trim().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
        }
        return input.trim();
    }

    public static String sanitizeName(String input) {
        return input == null ? null : input.trim().replaceAll("<[^>]*>", "");
    }

    public static String sanitizeEmail(String input) {
        if (input == null || !input.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        return input.trim().toLowerCase();
    }

    public static String sanitizeProfile(String input) {
        return input == null ? null : input.trim().replaceAll("[^a-zA-Z]", "");
    }

    public static String sanitizeCaptcha(String input) {
        return input == null ? null : input.trim().replaceAll("[^0-9]", "");
    }

    public static String sanitizeCode(String input) {
        return input == null ? null : input.trim().replaceAll("[^0-9]", "");
    }
}


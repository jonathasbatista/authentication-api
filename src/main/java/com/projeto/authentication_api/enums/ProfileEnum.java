package com.projeto.authentication_api.enums;

public enum ProfileEnum {
    ADMIN("admin"),
    USER("user");

    private String profile;

    ProfileEnum(String profile) {
        this.profile = profile;
    }
}

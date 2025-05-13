package com.projeto.authentication_api.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

//Responsável por representar as entidades no banco de dados
@Entity
@Table(name = "TB_USERS")
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String profile;
    private String ipAuthorized;

    public UserModel(){
    }

    public UserModel(String username, String password, String name, String email, String profile, String ipAuthorized) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.ipAuthorized = ipAuthorized;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getIpAuthorized() {
        return ipAuthorized;
    }

    public void setIpAuthorized(String ipAuthorized) {
        this.ipAuthorized = ipAuthorized;
    }
}

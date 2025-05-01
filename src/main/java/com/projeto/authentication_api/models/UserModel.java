package com.projeto.authentication_api.models;

import jakarta.persistence.*;
import com.projeto.authentication_api.enums.ProfileEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    @Enumerated(EnumType.STRING)
    private ProfileEnum profile;
    private String IPAuthorized;

    public UserModel(){
    }

    public UserModel(String username, String password, String name, String email, ProfileEnum profile) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.profile == ProfileEnum.ADMIN) {
            return List.of(new SimpleGrantedAuthority("PROFILE_ADMIN"));
        } else {
            return List.of(new SimpleGrantedAuthority("PROFILE_USER"));
        }
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

    public ProfileEnum getProfile() {
        return profile;
    }

    public void setProfile(ProfileEnum profile) {
        this.profile = profile;
    }

    public String getIPAuthorized() {
        return IPAuthorized;
    }

    public void setIPAuthorized(String IPAuthorized) {
        this.IPAuthorized = IPAuthorized;
    }
}

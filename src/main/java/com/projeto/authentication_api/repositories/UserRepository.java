package com.projeto.authentication_api.repositories;

import com.projeto.authentication_api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//Responsável pela integração com o banco de dados
@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
}

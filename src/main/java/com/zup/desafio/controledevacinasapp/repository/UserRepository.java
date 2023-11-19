package com.zup.desafio.controledevacinasapp.repository;


import com.zup.desafio.controledevacinasapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

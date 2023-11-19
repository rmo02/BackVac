package com.zup.desafio.controledevacinasapp.controller;

import com.zup.desafio.controledevacinasapp.DTO.UserLoginDTO;
import com.zup.desafio.controledevacinasapp.DTO.UserRegistrationDTO;
import com.zup.desafio.controledevacinasapp.entity.AuthResponse;
import com.zup.desafio.controledevacinasapp.entity.User;
import com.zup.desafio.controledevacinasapp.repository.UserRepository;
import com.zup.desafio.controledevacinasapp.service.TokenService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1")
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        System.out.println("Registrando usuário: " + userDTO.getUsername());

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(409).body("Usuário já existe!"); // Conflito
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());

        generateAndSetSalt(user);
        encodeAndSetPassword(user, userDTO.getPassword());

        userRepository.save(user);

        return ResponseEntity.status(201).body("Usuário registrado com sucesso!"); // Criado
    }

    private void generateAndSetSalt(User user) {
        String salt = generateSalt();
        user.setSalt(salt);
    }

    private void encodeAndSetPassword(User user, String password) {
        String passwordWithSalt = password + user.getSalt();
        String encodedPassword = passwordEncoder.encode(passwordWithSalt);
        user.setPassword(encodedPassword);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginDTO userDTO) {
        try {
            // Recupere o usuário do banco de dados com base no nome de usuário fornecido
            User storedUser = userRepository.findByUsername(userDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            // Combine a senha fornecida com o salt do usuário antes de aplicar o hash
            String passwordWithSalt = userDTO.getPassword() + storedUser.getSalt();

            // Verifique se a senha fornecida corresponde à senha armazenada no banco de dados
            if (passwordEncoder.matches(passwordWithSalt, storedUser.getPassword())) {
                // Autenticação bem-sucedida

                // Supondo que você tenha um método para gerar o token (pode ser o método existente em TokenService)
                String token = tokenService.generateToken(storedUser);

                // Construa a resposta de autenticação
                AuthResponse authResponse = new AuthResponse(storedUser.getUsername(), token);

                // Retorne a resposta com o status HTTP 200 (OK)
                return ResponseEntity.ok(authResponse);
            } else {
                // Senha incorreta
                return ResponseEntity.status(401).body(null); // Retornar 401 Unauthorized
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body(null); // Retornar 401 Unauthorized
        }
    }

    // Método para gerar um salt aleatório
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
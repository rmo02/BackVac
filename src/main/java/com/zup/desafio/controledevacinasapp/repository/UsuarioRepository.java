package com.zup.desafio.controledevacinasapp.repository;

import com.zup.desafio.controledevacinasapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findById(long id);

    Usuario findByEmail(String email);
    Usuario deleteById(long id);
}

package com.zup.desafio.controledevacinasapp.repository;

import com.zup.desafio.controledevacinasapp.entity.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {
    Responsavel findById(long id);
    Responsavel deleteById(long id);
}

package com.zup.desafio.controledevacinasapp.repository;

import com.zup.desafio.controledevacinasapp.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Medico findById(long id);
    Medico deleteById(long id);
}

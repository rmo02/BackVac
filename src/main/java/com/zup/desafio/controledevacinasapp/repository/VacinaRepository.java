package com.zup.desafio.controledevacinasapp.repository;

import com.zup.desafio.controledevacinasapp.entity.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VacinaRepository extends JpaRepository<Vacina,Long> {
    Vacina findById(long id);
    Vacina deleteById(long id);
}

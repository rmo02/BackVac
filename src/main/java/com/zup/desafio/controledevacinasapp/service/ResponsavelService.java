package com.zup.desafio.controledevacinasapp.service;

import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.entity.Responsavel;
import com.zup.desafio.controledevacinasapp.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;

    @Autowired
    public ResponsavelService(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    public Responsavel cadastrarResponsavel(Responsavel responsavel) {
        return responsavelRepository.save(responsavel);
    }

    public Responsavel salvar(Responsavel responsavel) {
        return responsavelRepository.save(responsavel);
    }

    public List<Responsavel> consultarTodos() {
        return responsavelRepository.findAll();
    }

    public Optional<Responsavel> consultarPorId(long id) {
        return Optional.ofNullable(responsavelRepository.findById(id));
    }


    public MensagemRespostaDTO deletarResponsavel(long id) {
        if (responsavelRepository.existsById(id)) {
            responsavelRepository.deleteById(id);
            String mensagem = "Responsável deletado com sucesso";
            return new MensagemRespostaDTO(mensagem);
        } else {
            String mensagem = "Responsável não encontrado";
            return new MensagemRespostaDTO(mensagem);
        }
    }
}
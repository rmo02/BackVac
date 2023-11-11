package com.zup.desafio.controledevacinasapp.controller;

import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.entity.Responsavel;
import com.zup.desafio.controledevacinasapp.service.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/responsavel")
public class ResponsavelController {

    private final ResponsavelService responsavelService;

    @Autowired
    public ResponsavelController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Responsavel> cadastrarResponsavel(@RequestBody @Valid Responsavel responsavel) {
        Responsavel responsavelCadastrado = responsavelService.cadastrarResponsavel(responsavel);
        return new ResponseEntity<>(responsavelCadastrado, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Responsavel> consultarTodos() {
        return responsavelService.consultarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Responsavel> consultarResponsavelPorId(@PathVariable("id") long id) {
        Optional<Responsavel> responsavelOptional = responsavelService.consultarPorId(id);

        return responsavelOptional.map(responsavel -> ResponseEntity.ok(responsavel))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemRespostaDTO> deletarResponsavel(@PathVariable("id") long id) {
        MensagemRespostaDTO respostaDTO = responsavelService.deletarResponsavel(id);
        return ResponseEntity.ok(respostaDTO);
    }
}

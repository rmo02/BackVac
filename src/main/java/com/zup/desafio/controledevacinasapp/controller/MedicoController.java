package com.zup.desafio.controledevacinasapp.controller;

import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.entity.Medico;
import com.zup.desafio.controledevacinasapp.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/medico")
public class MedicoController {

    private final MedicoService medicoService;

    @Autowired
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<Medico> cadastrarMedico(@RequestBody @Valid Medico medico) {
        Medico medicoCadastrado = medicoService.cadastrarMedico(medico);
        return new ResponseEntity<>(medicoCadastrado, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Medico> consultarTodos() {
        return medicoService.consultarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> consultarMedicoPorId(@PathVariable("id") long id) {
        Medico medico = medicoService.consultarPorId(id);
        if (medico != null) {
            return ResponseEntity.ok(medico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemRespostaDTO> deletarMedico(@PathVariable("id") long id) {
        MensagemRespostaDTO respostaDTO = medicoService.deletarMedico(id);
        return ResponseEntity.ok(respostaDTO);
    }

}

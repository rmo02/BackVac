package com.zup.desafio.controledevacinasapp.controller;

import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.DTO.VacinaDTO;
import com.zup.desafio.controledevacinasapp.entity.Vacina;
import com.zup.desafio.controledevacinasapp.service.VacinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vacina")
public class VacinaController {

    private final VacinaService vacinaService;

    @Autowired
    public VacinaController(VacinaService vacinaService) {
        this.vacinaService = vacinaService;
    }

    @PostMapping
    public ResponseEntity<MensagemRespostaDTO> cadastrarVacina(@RequestBody @Valid VacinaDTO vacinaDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MensagemRespostaDTO("Erro nos parâmetros de entrada"));
        }

        try {
            MensagemRespostaDTO respostaDTO = vacinaService.cadastrarVacinaAplicada(vacinaDTO.dtoParaVacina(), vacinaDTO.getMedicoId());
            return ResponseEntity.status(HttpStatus.CREATED).body(respostaDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagemRespostaDTO(e.getMessage()));
        }
    }

    @GetMapping
    public List<Vacina> consultarTodas() {
        return vacinaService.consultarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultarVacinacaoPorId(@PathVariable("id") long id) {
        Optional<Vacina> vacinaOptional = vacinaService.consultarPorId(id);

        if (vacinaOptional.isPresent()) {
            return ResponseEntity.ok(vacinaOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacina não encontrada");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemRespostaDTO> deletarCadastro(@PathVariable("id") long id) {
        MensagemRespostaDTO respostaDTO = vacinaService.deletarCadastro(id);
        return ResponseEntity.ok(respostaDTO);
    }
}

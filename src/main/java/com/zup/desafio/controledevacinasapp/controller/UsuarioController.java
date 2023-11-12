package com.zup.desafio.controledevacinasapp.controller;


import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.DTO.UsuarioDTO;
import com.zup.desafio.controledevacinasapp.entity.Responsavel;
import com.zup.desafio.controledevacinasapp.entity.Usuario;
import com.zup.desafio.controledevacinasapp.entity.Vacina;
import com.zup.desafio.controledevacinasapp.service.ResponsavelService;
import com.zup.desafio.controledevacinasapp.service.UsuarioService;
import com.zup.desafio.controledevacinasapp.service.VacinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/api/v1/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ResponsavelService responsavelService;

    private final VacinaService vacinaService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, ResponsavelService responsavelService, VacinaService vacinaService) {
        this.usuarioService = usuarioService;
        this.responsavelService = responsavelService;
        this.vacinaService = vacinaService;
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        Optional<Responsavel> responsavelOptional = responsavelService.consultarPorId(usuarioDTO.getResponsavelId());

        if (responsavelOptional.isPresent()) {
            Responsavel responsavel = responsavelOptional.get();

            // Crie o usuário, mas não salve ainda
            Usuario usuario = usuarioDTO.dtoParaUsuario();
            usuario.setResponsavel(responsavel);

            // Verifique se o usuário tem vacinas
            if (usuarioDTO.getVacina() != null) {
                usuarioDTO.getVacina().forEach(vacinaDTO -> {
                    Vacina vacina = vacinaDTO.dtoParaVacina();
                    vacina.setUsuario(usuario);
                    vacinaService.cadastrarVacinaAplicada(vacina, vacinaDTO.getMedicoId());
                });
            }

            // Agora salve o usuário
            Usuario usuarioSalvo = usuarioService.salvar(usuario);

            if (usuarioSalvo == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o usuário");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Responsavel não encontrado");
        }
    }

    @GetMapping
    public List<Usuario> consultarTodos(){
        return usuarioService.consultarTodos();
    }

    @GetMapping(path = "/{id}")
    public Usuario consultarUsuario(@PathVariable("id") long id){
        return usuarioService.consultarUsuario(id);
    }


    @DeleteMapping(path = "/{id}")
    public MensagemRespostaDTO deletarUsuario(@PathVariable("id") long id){
        return usuarioService.deletarUsuario(id);
        }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario atualizarUsuario(@PathVariable("id") long id, @RequestBody @Valid UsuarioDTO usuarioDTO){
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO.dtoParaUsuario());
        return usuarioAtualizado;
    }


}


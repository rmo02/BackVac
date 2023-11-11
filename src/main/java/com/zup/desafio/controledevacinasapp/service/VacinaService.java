package com.zup.desafio.controledevacinasapp.service;

import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.entity.Medico;
import com.zup.desafio.controledevacinasapp.entity.Usuario;
import com.zup.desafio.controledevacinasapp.entity.Vacina;
import com.zup.desafio.controledevacinasapp.repository.MedicoRepository;
import com.zup.desafio.controledevacinasapp.repository.UsuarioRepository; // Adicionei o repositório do Usuario
import com.zup.desafio.controledevacinasapp.repository.VacinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VacinaService {

    private final VacinaRepository vacinaRepository;
    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository; // Adicionei o repositório do Usuario

    @Autowired
    public VacinaService(VacinaRepository vacinaRepository, MedicoRepository medicoRepository, UsuarioRepository usuarioRepository) {
        this.vacinaRepository = vacinaRepository;
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Início dos métodos CRUD

    public MensagemRespostaDTO cadastrarVacinaAplicada(Vacina vacina, Long medicoId) {
        Medico medico = medicoRepository.findById(medicoId).orElse(null);

        if (medico != null) {
            vacina.setMedico(medico);
            vacina.setAplicado(true);

            Usuario usuario = usuarioRepository.findByEmail(vacina.getEmail());

            if (usuario != null) {
                vacina.setUsuario(usuario);
                vacinaRepository.save(vacina);

                String mensagem = "Vacina cadastrada com sucesso";
                return new MensagemRespostaDTO(mensagem);
            } else {
                String mensagem = "Usuário não encontrado";
                return new MensagemRespostaDTO(mensagem);
            }
        } else {
            String mensagem = "Médico não encontrado";
            return new MensagemRespostaDTO(mensagem);
        }
    }

    public List<Vacina> consultarTodas() {
        return vacinaRepository.findAll();
    }

    public Optional<Vacina> consultarPorId(Long id) {
        return vacinaRepository.findById(id);
    }

    public MensagemRespostaDTO deletarCadastro(long id) {
        vacinaRepository.deleteById(id);
        String mensagem = "Cadastro deletado com sucesso";
        return new MensagemRespostaDTO(mensagem);
    }
}

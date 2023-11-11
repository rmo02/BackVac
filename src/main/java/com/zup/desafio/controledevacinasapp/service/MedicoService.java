package com.zup.desafio.controledevacinasapp.service;

import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.entity.Medico;
import com.zup.desafio.controledevacinasapp.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    @Autowired
    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    // Método para cadastrar um médico
    public Medico cadastrarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    // Método para consultar todos os médicos
    public List<Medico> consultarTodos() {
        return medicoRepository.findAll();
    }

    // Método para consultar um médico por ID
    public Medico consultarPorId(long id) {
        Optional<Medico> medicoOptional = Optional.ofNullable(medicoRepository.findById(id));
        return medicoOptional.orElse(null);
    }

    // Método para deletar um médico
    public MensagemRespostaDTO deletarMedico(long id) {
        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            String mensagem = "Médico deletado com sucesso";
            return new MensagemRespostaDTO(mensagem);
        } else {
            String mensagem = "Médico não encontrado";
            return new MensagemRespostaDTO(mensagem);
        }
    }

    // Método para atualizar um médico
    public Medico atualizarMedico(long id, Medico medicoAtualizado) {
        Optional<Medico> medicoExistenteOptional = Optional.ofNullable(medicoRepository.findById(id));

        if (medicoExistenteOptional.isPresent()) {
            Medico medicoExistente = medicoExistenteOptional.get();

            // Atualiza os campos necessários do médicoExistente com as informações do medicoAtualizado
            medicoExistente.setNome(medicoAtualizado.getNome());
            medicoExistente.setEmail(medicoAtualizado.getEmail());
            medicoExistente.setCPF(medicoAtualizado.getCPF());
            medicoExistente.setCRM(medicoAtualizado.getCRM());

            return medicoRepository.save(medicoExistente);
        } else {
            // Lida com o caso em que o médico não foi encontrado
            return null;
        }
    }
}

package com.zup.desafio.controledevacinasapp.service;

import com.zup.desafio.controledevacinasapp.DTO.MensagemRespostaDTO;
import com.zup.desafio.controledevacinasapp.entity.Usuario;
import com.zup.desafio.controledevacinasapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Inicio dos métodos CRUD

    public Usuario salvar(Usuario usuario) {
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Set the user in each vaccine
        if (usuario.getVacinas() != null) {
            usuario.getVacinas().forEach(vacina -> vacina.setUsuario(savedUsuario));
        }

        return savedUsuario;
    }

    public List<Usuario> consultarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario consultarUsuario(long id) {
        Optional<Usuario> usuarioOptional = Optional.ofNullable(usuarioRepository.findById(id));
        return usuarioOptional.orElse(null);
    }

    public MensagemRespostaDTO deletarUsuario(long id) {
        usuarioRepository.deleteById(id);
        String mensagem = "Usuário deletado com Sucesso";
        return new MensagemRespostaDTO(mensagem);
    }

    public Usuario atualizarUsuario(long id , Usuario usuario) {
        Optional<Usuario> usuarioOptional = Optional.ofNullable(usuarioRepository.findById(id));


        if (usuarioOptional.isPresent()) {
            Usuario atualizarUsuario = usuarioOptional.get();
            atualizarUsuario.setNome(usuario.getNome());
            atualizarUsuario.setCPF(usuario.getCPF());
            atualizarUsuario.setNumTelefone(usuario.getNumTelefone());
            atualizarUsuario.setDataNascimento(usuario.getDataNascimento());
            return usuarioRepository.save(atualizarUsuario);
        } else {
            // Trate o caso em que o usuário não foi encontrado, lançando uma exceção ou retornando null, conforme necessário
            return null;
        }
    }
}

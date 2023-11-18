package com.zup.desafio.controledevacinasapp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zup.desafio.controledevacinasapp.entity.Usuario;
import com.zup.desafio.controledevacinasapp.entity.Vacina;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VacinaDTO {

    @NotBlank(message = "CPF não pode estar em branco")
    private String CPF;

    @NotBlank(message = "Nome da vacina não pode estar em branco")
    private String nomeVacina;

    @NotNull(message = "Data de vacinação não pode ser nula")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataVacinacao;

    @NotNull(message = "Id do médico não pode ser nulo")
    private Long medicoId;

    private Long usuarioId;

    private boolean aplicado;

    public VacinaDTO() {}

    public VacinaDTO(String CPF, String nomeVacina, LocalDate dataVacinacao, Long medicoId, boolean aplicado, Long usuarioId) {
        this.CPF = CPF;
        this.nomeVacina = nomeVacina;
        this.dataVacinacao = dataVacinacao;
        this.medicoId = medicoId;
        this.aplicado = aplicado;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    public LocalDate getDataVacinacao() {
        return dataVacinacao;
    }

    public void setDataVacinacao(LocalDate dataVacinacao) {
        this.dataVacinacao = dataVacinacao;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean isAplicado() {
        return aplicado;
    }

    public void setAplicado(boolean aplicado) {
        this.aplicado = aplicado;
    }

    public Vacina dtoParaVacina() {
        Vacina vacina = new Vacina();
        vacina.setCPF(CPF);
        vacina.setNomeVacina(nomeVacina);
        vacina.setDataVacinacao(dataVacinacao);
        vacina.setAplicado(aplicado);

        if (usuarioId != null) {
            // Certifique-se de que o usuário com o ID fornecido existe no banco de dados
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            vacina.setUsuario(usuario);
        }

        return vacina;
    }
}

package com.zup.desafio.controledevacinasapp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zup.desafio.controledevacinasapp.entity.Responsavel;
import com.zup.desafio.controledevacinasapp.entity.Usuario;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class UsuarioDTO {
    private Long id;
    private Long responsavelId;
    private List<VacinaDTO> vacina;

    @NotBlank
    @Size(max = 200)
    private String nome;

    @NotBlank(message = "Digite um email")
    @Email(message = "Digite um email válido")
    private String email;

    @NotBlank(message = "Digite um CPF")
    @CPF(message = "Por favor, Digite um CPF válido.")
    private String CPF;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataNascimento;

    public UsuarioDTO() {}

    public UsuarioDTO(String nome, String email, String CPF, LocalDate dataNascimento, Long responsavelId) {
        this.nome = nome;
        this.email = email;
        this.CPF = CPF;
        this.dataNascimento = dataNascimento;
        this.responsavelId = responsavelId;
    }

    public Usuario dtoParaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(id);

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setCPF(CPF);
        usuario.setDataNascimento(dataNascimento);

        if (responsavelId != null) {
            Responsavel responsavel = new Responsavel();
            responsavel.setId(responsavelId);
            usuario.setResponsavel(responsavel);
        }

        return usuario;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public List<VacinaDTO> getVacina() {
        return vacina;
    }

    public void setVacina(List<VacinaDTO> vacina) {
        this.vacina = vacina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioDTO)) return false;
        UsuarioDTO that = (UsuarioDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getNome(), that.getNome()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getCPF(), that.getCPF()) && Objects.equals(getDataNascimento(), that.getDataNascimento());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNome(), getEmail(), getCPF(), getDataNascimento());
    }
}

package com.zup.desafio.controledevacinasapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "VACINAS")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_usuario", nullable = false)
    private String email;

    @Column(nullable = false)
    private String nomeVacina;

    @Column(nullable = false)
    private LocalDate dataVacinacao;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private boolean aplicado;

    // Construtor padrão
    public Vacina() {
    }

    // Construtor com todos os atributos
    public Vacina(Long id, String email, String nomeVacina, LocalDate dataVacinacao, Medico medico, Usuario usuario, boolean aplicado) {
        this.id = id;
        this.email = email;
        this.nomeVacina = nomeVacina;
        this.dataVacinacao = dataVacinacao;
        this.medico = medico;
        this.usuario = usuario;
        this.aplicado = aplicado;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @JsonIgnore
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isAplicado() {
        return aplicado;
    }

    public void setAplicado(boolean aplicado) {
        this.aplicado = aplicado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacina)) return false;
        Vacina vacina = (Vacina) o;
        return id.equals(vacina.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

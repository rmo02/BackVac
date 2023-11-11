package com.zup.desafio.controledevacinasapp.DTO;

public class MensagemRespostaDTO {

    String mensagem;

    public MensagemRespostaDTO(String deletado) {
        this.mensagem = deletado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

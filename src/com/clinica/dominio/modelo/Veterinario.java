package com.clinica.dominio.modelo;

import com.clinica.dominio.excecao.VeterinarioIndisponivelException;

public class Veterinario {
    public enum Situacao {
        DISPONIVEL,
        OCUPADO
    }

    private Long id;
    private final String nome;
    private final String crmv;
    private final String especialidade;
    private Situacao situacao;

    public Veterinario(Long id, String nome, String crmv, String especialidade, Situacao situacao) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do veterinário é obrigatório.");
        }

        if (crmv == null || crmv.isBlank()) {
            throw new IllegalArgumentException("O CRMV do veterinário é obrigatório.");
        }

        if (especialidade == null || especialidade.isBlank()) {
            throw new IllegalArgumentException("A especialidade do veterinário é obrigatória.");
        }

        this.id = id;
        this.nome = nome;
        this.crmv = crmv;
        this.especialidade = especialidade;
        this.situacao = situacao == null ? Situacao.DISPONIVEL : situacao;
    }

    public boolean estaDisponivel() {
        return situacao == Situacao.DISPONIVEL;
    }

    public void ocupar() {
        if (!estaDisponivel()) {
            throw new VeterinarioIndisponivelException(
                "Veterinário indisponível para atendimento. Situação atual: " + situacao
            );
        }

        this.situacao = Situacao.OCUPADO;
    }

    public void liberar() {
        this.situacao = Situacao.DISPONIVEL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null && !this.id.equals(id)) {
            throw new IllegalStateException("O id do veterinário não pode ser alterado depois de definido.");
        }

        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getCrmv() {
        return crmv;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public Situacao getSituacao() {
        return situacao;
    }
}

package com.clinica.dominio.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    private Long id;
    private final Animal animal;
    private final Veterinario veterinario;
    private final LocalDate data;
    private final LocalTime hora;
    private final TipoConsulta tipo;
    private SituacaoConsulta situacao;
    private String observacoes;

    public Consulta(
        Long id,
        Animal animal,
        Veterinario veterinario,
        LocalDate data,
        LocalTime hora,
        TipoConsulta tipo,
        SituacaoConsulta situacao,
        String observacoes
    ) {
        if (animal == null) {
            throw new IllegalArgumentException("A consulta deve estar associada a um animal.");
        }

        if (veterinario == null) {
            throw new IllegalArgumentException("A consulta deve estar associada a um veterinário.");
        }

        if (data == null) {
            throw new IllegalArgumentException("A data da consulta é obrigatória.");
        }

        if (hora == null) {
            throw new IllegalArgumentException("A hora da consulta é obrigatória.");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("O tipo da consulta é obrigatório.");
        }

        this.id = id;
        this.animal = animal;
        this.veterinario = veterinario;
        this.data = data;
        this.hora = hora;
        this.tipo = tipo;
        this.situacao = situacao == null ? SituacaoConsulta.AGENDADA : situacao;
        this.observacoes = observacoes;
    }

    public void realizar(String observacoes) {
        if (this.situacao != SituacaoConsulta.AGENDADA) {
            throw new IllegalStateException(
                "Apenas consultas AGENDADAS podem ser realizadas. Situação atual: " + this.situacao
            );
        }

        this.situacao = SituacaoConsulta.REALIZADA;
        this.observacoes = observacoes;
    }

    public void cancelar() {
        if (this.situacao != SituacaoConsulta.AGENDADA && this.situacao != SituacaoConsulta.REALIZADA) {
            throw new IllegalStateException(
                "Apenas consultas AGENDADAS ou REALIZADAS podem ser canceladas. Situação atual: " + this.situacao
            );
        }

        this.situacao = SituacaoConsulta.CANCELADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null && !this.id.equals(id)) {
            throw new IllegalStateException("O id da consulta não pode ser alterado depois de definido.");
        }

        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public TipoConsulta getTipo() {
        return tipo;
    }

    public SituacaoConsulta getSituacao() {
        return situacao;
    }

    public String getObservacoes() {
        return observacoes;
    }
}

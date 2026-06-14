package com.clinica.dominio.modelo;

import java.time.LocalDate;
import java.time.Period;

public class Animal {
    private Long id;
    private final String nome;
    private final String especie;
    private final String raca;
    private final LocalDate dataNascimento;
    private final String tutor;

    public Animal(Long id, String nome, String especie, String raca, LocalDate dataNascimento, String tutor) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do animal é obrigatório.");
        }

        if (especie == null || especie.isBlank()) {
            throw new IllegalArgumentException("A espécie do animal é obrigatória.");
        }

        if (tutor == null || tutor.isBlank()) {
            throw new IllegalArgumentException("O nome do tutor é obrigatório.");
        }

        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.tutor = tutor;
    }

    public int calcularIdadeEmAnos() {
        if (dataNascimento == null) {
            return 0;
        }

        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null && !this.id.equals(id)) {
            throw new IllegalStateException("O id do animal não pode ser alterado depois de definido.");
        }

        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaca() {
        return raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getTutor() {
        return tutor;
    }
}

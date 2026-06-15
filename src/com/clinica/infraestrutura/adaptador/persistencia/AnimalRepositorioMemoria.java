package com.clinica.infraestrutura.adaptador.persistencia;

import com.clinica.dominio.modelo.Animal;
import com.clinica.dominio.porta.saida.PortaAnimalRepositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnimalRepositorioMemoria implements PortaAnimalRepositorio {
    private final Map<Long, Animal> store = new HashMap<>();
    private long proximoId = 1L;

    @Override
    public void salvar(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal não pode ser nulo.");
        }

        if (animal.getId() == null) {
            animal.setId(proximoId++);
        } else if (animal.getId() >= proximoId) {
            proximoId = animal.getId() + 1;
        }

        store.put(animal.getId(), animal);
    }

    @Override
    public Optional<Animal> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Animal> listarPorTutor(String tutor) {
        if (tutor == null) {
            return List.of();
        }

        return store.values().stream()
            .filter(animal -> animal.getTutor().equalsIgnoreCase(tutor))
            .collect(Collectors.toList());
    }

    @Override
    public List<Animal> listarTodos() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void remover(Long id) {
        store.remove(id);
    }
}

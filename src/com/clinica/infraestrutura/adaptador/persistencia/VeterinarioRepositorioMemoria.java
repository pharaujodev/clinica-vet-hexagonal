package com.clinica.infraestrutura.adaptador.persistencia;

import com.clinica.dominio.modelo.Veterinario;
import com.clinica.dominio.porta.saida.PortaVeterinarioRepositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class VeterinarioRepositorioMemoria implements PortaVeterinarioRepositorio {
    private final Map<Long, Veterinario> store = new HashMap<>();
    private long proximoId = 1L;

    @Override
    public void salvar(Veterinario veterinario) {
        if (veterinario == null) {
            throw new IllegalArgumentException("Veterinário não pode ser nulo.");
        }

        if (veterinario.getId() == null) {
            veterinario.setId(proximoId++);
        } else if (veterinario.getId() >= proximoId) {
            proximoId = veterinario.getId() + 1;
        }

        store.put(veterinario.getId(), veterinario);
    }

    @Override
    public Optional<Veterinario> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Veterinario> buscarDisponiveis() {
        return store.values().stream()
            .filter(Veterinario::estaDisponivel)
            .collect(Collectors.toList());
    }

    @Override
    public List<Veterinario> buscarPorEspecialidade(String especialidade) {
        if (especialidade == null) {
            return List.of();
        }

        return store.values().stream()
            .filter(veterinario -> veterinario.getEspecialidade().equalsIgnoreCase(especialidade))
            .collect(Collectors.toList());
    }

    public List<Veterinario> listarTodos() {
        return new ArrayList<>(store.values());
    }
}

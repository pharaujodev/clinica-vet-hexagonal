package com.clinica.infraestrutura.adaptador.persistencia;

import com.clinica.dominio.modelo.Consulta;
import com.clinica.dominio.modelo.SituacaoConsulta;
import com.clinica.dominio.porta.saida.PortaConsultaRepositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConsultaRepositorioMemoria implements PortaConsultaRepositorio {
    private final Map<Long, Consulta> store = new HashMap<>();
    private long proximoId = 1L;

    @Override
    public void salvar(Consulta consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("Consulta não pode ser nula.");
        }

        if (consulta.getId() == null) {
            consulta.setId(proximoId++);
        } else if (consulta.getId() >= proximoId) {
            proximoId = consulta.getId() + 1;
        }

        store.put(consulta.getId(), consulta);
    }

    @Override
    public Optional<Consulta> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Consulta> buscarPorAnimal(Long animalId) {
        return store.values().stream()
            .filter(consulta -> consulta.getAnimal().getId().equals(animalId))
            .collect(Collectors.toList());
    }

    @Override
    public List<Consulta> buscarPorVeterinario(Long veterinarioId) {
        return store.values().stream()
            .filter(consulta -> consulta.getVeterinario().getId().equals(veterinarioId))
            .collect(Collectors.toList());
    }

    @Override
    public List<Consulta> listarAgendadas() {
        return store.values().stream()
            .filter(consulta -> consulta.getSituacao() == SituacaoConsulta.AGENDADA)
            .collect(Collectors.toList());
    }

    public List<Consulta> listarTodos() {
        return new ArrayList<>(store.values());
    }
}

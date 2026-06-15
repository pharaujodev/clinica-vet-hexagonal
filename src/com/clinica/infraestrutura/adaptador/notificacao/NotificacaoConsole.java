package com.clinica.infraestrutura.adaptador.notificacao;

import com.clinica.dominio.modelo.Animal;
import com.clinica.dominio.modelo.Consulta;
import com.clinica.dominio.porta.saida.PortaNotificacaoTutor;

public class NotificacaoConsole implements PortaNotificacaoTutor {
    @Override
    public void notificarAgendamento(String tutor, Animal animal, Consulta consulta) {
        System.out.println(
            "[AGENDAMENTO] Tutor: " + tutor +
            " | Animal: " + animal.getNome() +
            " (" + animal.getRaca() + ")" +
            " | Vet.: " + consulta.getVeterinario().getNome() +
            " (" + consulta.getVeterinario().getEspecialidade() + ")" +
            " | Data: " + consulta.getData() +
            " às " + consulta.getHora() +
            " | Tipo: " + consulta.getTipo()
        );
    }

    @Override
    public void notificarCancelamento(String tutor, Animal animal, String motivo) {
        System.out.println(
            "[CANCELAMENTO] Tutor: " + tutor +
            " | Animal: " + animal.getNome() +
            " | Motivo: " + motivo
        );
    }
}

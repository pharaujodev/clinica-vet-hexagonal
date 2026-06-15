package com.clinica.apresentacao;

import com.clinica.dominio.modelo.Animal;
import com.clinica.dominio.modelo.Consulta;
import com.clinica.dominio.modelo.TipoConsulta;
import com.clinica.dominio.modelo.Veterinario;
import com.clinica.dominio.porta.entrada.PortaAgendaConsulta;
import com.clinica.dominio.porta.saida.PortaAnimalRepositorio;
import com.clinica.dominio.porta.saida.PortaConsultaRepositorio;
import com.clinica.dominio.porta.saida.PortaNotificacaoTutor;
import com.clinica.dominio.porta.saida.PortaVeterinarioRepositorio;
import com.clinica.dominio.servico.ServicoAgendaConsulta;
import com.clinica.infraestrutura.adaptador.notificacao.NotificacaoConsole;
import com.clinica.infraestrutura.adaptador.notificacao.NotificacaoCsv;
import com.clinica.infraestrutura.adaptador.persistencia.AnimalRepositorioMemoria;
import com.clinica.infraestrutura.adaptador.persistencia.ConsultaRepositorioMemoria;
import com.clinica.infraestrutura.adaptador.persistencia.VeterinarioRepositorioMemoria;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PortaAnimalRepositorio animais = new AnimalRepositorioMemoria();
        PortaVeterinarioRepositorio veterinarios = new VeterinarioRepositorioMemoria();
        PortaConsultaRepositorio consultas = new ConsultaRepositorioMemoria();

        PortaNotificacaoTutor notificacaoConsole = new NotificacaoConsole();
        PortaAgendaConsulta agendaConsole = new ServicoAgendaConsulta(
            animais,
            veterinarios,
            consultas,
            notificacaoConsole
        );

        PortaNotificacaoTutor notificacaoCsv = new NotificacaoCsv("notificacoes.csv");
        PortaAgendaConsulta agendaCsv = new ServicoAgendaConsulta(
            animais,
            veterinarios,
            consultas,
            notificacaoCsv
        );

        Animal apolo = new Animal(
            null,
            "Apolo",
            "Cachorro",
            "Golden Retriever",
            LocalDate.of(2020, 4, 12),
            "Pedro Henrique"
        );

        Animal mel = new Animal(
            null,
            "Mel",
            "Gato",
            "Siamês",
            LocalDate.of(2021, 9, 3),
            "Maria Araujo"
        );

        animais.salvar(apolo);
        animais.salvar(mel);

        Veterinario joaoVitor = new Veterinario(
            null,
            "Dr. João Vitor",
            "CRMV-GO 12345",
            "Clínica Geral",
            Veterinario.Situacao.DISPONIVEL
        );

        Veterinario anaClara = new Veterinario(
            null,
            "Dra. Ana Clara",
            "CRMV-GO 67890",
            "Emergência",
            Veterinario.Situacao.DISPONIVEL
        );

        veterinarios.salvar(joaoVitor);
        veterinarios.salvar(anaClara);

        System.out.println("=== Sistema de Gerenciamento de Clínica Veterinária ===");
        System.out.println("Fluxo de demonstração da arquitetura hexagonal");
        System.out.println();

        Consulta consultaRotina = agendaConsole.agendarConsulta(
            apolo.getId(),
            joaoVitor.getId(),
            LocalDate.of(2025, 7, 15),
            LocalTime.of(14, 30),
            TipoConsulta.ROTINA
        );

        Consulta consultaEmergencia = agendaCsv.agendarConsulta(
            mel.getId(),
            anaClara.getId(),
            LocalDate.of(2025, 7, 16),
            LocalTime.of(9, 0),
            TipoConsulta.EMERGENCIA
        );

        System.out.println(
            "[AGENDAMENTO CSV] Consulta #" + consultaEmergencia.getId() +
            " registrada em notificacoes.csv para o tutor Maria Araujo."
        );

        Consulta consultaRealizada = agendaConsole.realizarConsulta(
            consultaRotina.getId(),
            "Apolo apresentou bom estado geral. Vacinação e alimentação orientadas ao tutor."
        );

        System.out.println(
            "[CONSULTA REALIZADA] Animal: " +
            consultaRealizada.getAnimal().getNome() +
            " | Tutor: " + consultaRealizada.getAnimal().getTutor() +
            " | Obs.: " + consultaRealizada.getObservacoes()
        );

        agendaConsole.cancelarConsulta(consultaEmergencia.getId());

        System.out.println();

        exibirHistoricoAnimal(apolo.getNome(), agendaConsole.obterHistoricoAnimal(apolo.getId()));
        System.out.println();
        exibirAgendaVeterinario(joaoVitor.getNome(), agendaConsole.obterAgendaVeterinario(joaoVitor.getId()));
    }

    private static void exibirHistoricoAnimal(String nomeAnimal, List<Consulta> historico) {
        System.out.println("=== Histórico de " + nomeAnimal + " ===");

        if (historico.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada.");
            return;
        }

        for (Consulta consulta : historico) {
            imprimirConsulta(consulta);
        }
    }

    private static void exibirAgendaVeterinario(String nomeVeterinario, List<Consulta> agenda) {
        System.out.println("=== Agenda de " + nomeVeterinario + " ===");

        if (agenda.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada.");
            return;
        }

        for (Consulta consulta : agenda) {
            imprimirConsulta(consulta);
        }
    }

    private static void imprimirConsulta(Consulta consulta) {
        System.out.println(
            "Consulta #" + consulta.getId() +
            " — " + consulta.getData() +
            " " + consulta.getHora() +
            " | " + consulta.getTipo() +
            " | " + consulta.getSituacao()
        );
    }
}

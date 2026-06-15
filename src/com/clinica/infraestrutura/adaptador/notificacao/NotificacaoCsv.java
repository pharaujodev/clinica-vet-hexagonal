package com.clinica.infraestrutura.adaptador.notificacao;

import com.clinica.dominio.modelo.Animal;
import com.clinica.dominio.modelo.Consulta;
import com.clinica.dominio.porta.saida.PortaNotificacaoTutor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class NotificacaoCsv implements PortaNotificacaoTutor {
    private final String caminhoArquivo;

    public NotificacaoCsv(String caminhoArquivo) {
        if (caminhoArquivo == null || caminhoArquivo.isBlank()) {
            throw new IllegalArgumentException("O caminho do arquivo CSV é obrigatório.");
        }

        this.caminhoArquivo = caminhoArquivo;
        criarCabecalhoSeNecessario();
    }

    @Override
    public void notificarAgendamento(String tutor, Animal animal, Consulta consulta) {
        String dataConsulta = consulta.getData() + "T" + consulta.getHora();

        escreverLinha(
            "AGENDAMENTO",
            tutor,
            animal.getNome(),
            consulta.getVeterinario().getNome(),
            dataConsulta
        );
    }

    @Override
    public void notificarCancelamento(String tutor, Animal animal, String motivo) {
        escreverLinha(
            "CANCELAMENTO",
            tutor,
            animal.getNome(),
            "N/A",
            "N/A"
        );
    }

    private void criarCabecalhoSeNecessario() {
        File arquivo = new File(caminhoArquivo);

        if (arquivo.exists() && arquivo.length() > 0) {
            return;
        }

        try (FileWriter writer = new FileWriter(arquivo, true)) {
            writer.write("timestamp,tipo_evento,tutor,animal,veterinario,data_consulta\n");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar cabeçalho do arquivo de notificações.", e);
        }
    }

    private void escreverLinha(
        String tipoEvento,
        String tutor,
        String animal,
        String veterinario,
        String dataConsulta
    ) {
        try (FileWriter writer = new FileWriter(caminhoArquivo, true)) {
            writer.write(escapar(LocalDateTime.now().toString()));
            writer.write(",");
            writer.write(escapar(tipoEvento));
            writer.write(",");
            writer.write(escapar(tutor));
            writer.write(",");
            writer.write(escapar(animal));
            writer.write(",");
            writer.write(escapar(veterinario));
            writer.write(",");
            writer.write(escapar(dataConsulta));
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao registrar notificação em CSV.", e);
        }
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }

        String texto = valor.replace("\"", "\"\"");

        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n")) {
            texto = "\"" + texto + "\"";
        }

        return texto;
    }
}

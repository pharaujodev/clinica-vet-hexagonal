# Sistema de Gerenciamento de Clínica Veterinária

Atividade prática de **Arquitetura Hexagonal com Java**, desenvolvida com Java puro, sem frameworks externos.

## Discente

Pedro Henrique de Araujo Pereira

## Objetivo

O objetivo do projeto é simular o gerenciamento de consultas de uma clínica veterinária, mantendo o domínio da aplicação isolado da infraestrutura e da apresentação.

A aplicação permite:

- cadastrar animais;
- cadastrar veterinários;
- agendar consultas;
- realizar consultas;
- cancelar consultas;
- consultar o histórico de um animal;
- consultar a agenda de um veterinário;
- demonstrar a troca de adaptadores de notificação sem alterar o domínio.

## Arquitetura utilizada

O projeto segue o padrão **Arquitetura Hexagonal (Ports and Adapters)**.

A ideia principal é separar o núcleo da aplicação das tecnologias externas. Por isso, o pacote `dominio` não depende de classes de infraestrutura nem de apresentação.

Estrutura principal:

```text
src/com/clinica/
├── dominio/
│   ├── modelo/
│   ├── excecao/
│   ├── porta/
│   │   ├── entrada/
│   │   └── saida/
│   └── servico/
├── infraestrutura/
│   └── adaptador/
│       ├── persistencia/
│       └── notificacao/
└── apresentacao/
```

## Domínio

O pacote `dominio` contém as regras centrais do sistema.

Principais classes:

- `Animal`: representa o animal atendido pela clínica e calcula a idade com base na data de nascimento.
- `Veterinario`: representa o profissional responsável pelas consultas e controla sua disponibilidade.
- `Consulta`: representa uma consulta veterinária e controla as transições de estado.
- `ServicoAgendaConsulta`: implementa os casos de uso da aplicação por meio das portas.

As entidades são POJOs e não possuem anotações ou dependências de frameworks.

## Portas de entrada

A porta de entrada define os casos de uso oferecidos pela aplicação.

- `PortaAgendaConsulta`

Ela é implementada por:

- `ServicoAgendaConsulta`

Casos de uso implementados:

- `agendarConsulta`
- `realizarConsulta`
- `cancelarConsulta`
- `obterHistoricoAnimal`
- `obterAgendaVeterinario`

## Portas de saída

As portas de saída representam as dependências externas necessárias ao domínio, sem acoplar o domínio a uma implementação concreta.

Portas implementadas:

- `PortaAnimalRepositorio`
- `PortaVeterinarioRepositorio`
- `PortaConsultaRepositorio`
- `PortaNotificacaoTutor`

## Adaptadores de persistência

Os repositórios em memória ficam no pacote `infraestrutura/adaptador/persistencia`.

Adaptadores implementados:

- `AnimalRepositorioMemoria`
- `VeterinarioRepositorioMemoria`
- `ConsultaRepositorioMemoria`

Eles usam `HashMap` como armazenamento interno e implementam as portas de saída do domínio.

## Adaptadores de notificação

Os adaptadores de notificação ficam no pacote `infraestrutura/adaptador/notificacao`.

Adaptadores implementados:

- `NotificacaoConsole`: exibe notificações no terminal.
- `NotificacaoCsv`: registra notificações no arquivo `notificacoes.csv`.

A troca entre esses adaptadores acontece no `Main`, sem alterar o serviço de domínio.

## Apresentação

A classe `Main` é o ponto de composição da aplicação.

Ela instancia os adaptadores concretos, conecta as portas ao serviço de domínio e executa o fluxo de demonstração:

1. cadastro de dois animais;
2. cadastro de dois veterinários;
3. agendamento de consulta com notificação no console;
4. agendamento de consulta com notificação em CSV;
5. realização de consulta;
6. cancelamento de consulta;
7. exibição do histórico do animal;
8. exibição da agenda do veterinário.

## Como compilar e executar

A compilação e execução podem ser feitas com Java puro, a partir do diretório `src/`.

### Linux, macOS ou Git Bash

```bash
cd src
find . -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out com.clinica.apresentacao.Main
```

### PowerShell no Windows

No PowerShell, o comando abaixo evita problemas com o caractere `@` e com a codificação do arquivo `sources.txt`.

```powershell
cd src
Remove-Item -Recurse -Force out,sources.txt -ErrorAction SilentlyContinue
$arquivos = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $arquivos
java -cp out com.clinica.apresentacao.Main
```

## Arquivos gerados

Durante a execução, o adaptador CSV pode criar o arquivo:

```text
notificacoes.csv
```

Esse arquivo é gerado em tempo de execução e não precisa ser versionado.

## Decisões arquiteturais

- O domínio depende apenas de suas próprias classes e de interfaces definidas no pacote `dominio.porta`.
- Os adaptadores concretos ficam fora do domínio, dentro de `infraestrutura`.
- O `Main` é o único ponto onde as implementações concretas são instanciadas.
- A notificação por console e por CSV demonstra a troca de adaptadores sem alterar o serviço de domínio.
- As regras de estado de `Veterinario` e `Consulta` ficam dentro das próprias entidades.
- Os repositórios usam `Optional` para tratar ausência de dados sem retornar `null`.

## Observação

Este projeto foi desenvolvido com Java puro, sem Spring, JPA, Hibernate, Jackson ou outras bibliotecas externas.

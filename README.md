# Sistema de Gerenciamento de Biblioteca

Um projeto Java implementando **Arquitetura de Software** em três etapas progressivas: Arquitetura em Camadas, Arquitetura Hexagonal (Ports and Adapters) e Comunicação Assíncrona por Eventos.

## Descrição

Este sistema demonstra os princípios de arquitetura de software através da implementação de um gerenciador de biblioteca capaz de:
- Cadastrar livros e usuários
- Realizar empréstimos com verificação de disponibilidade
- Registrar devoluções
- Detectar atrasos em empréstimos
- Notificar sobre operações via sistema de eventos

## Compilação

Para compilar o projeto, execute na raiz do repositório:

```bash
javac -d bin \
  src/biblioteca/dominio/*.java \
  src/biblioteca/dominio/evento/*.java \
  src/biblioteca/porta/entrada/*.java \
  src/biblioteca/porta/saida/*.java \
  src/biblioteca/infraestrutura/adaptador/*.java \
  src/biblioteca/servico/*.java \
  src/biblioteca/apresentacao/*.java
```

### Requisitos
- Java 17 ou superior
- Nenhuma dependência externa (apenas bibliotecas padrão do Java)

## Execução

Para executar o programa compilado:

```bash
java -cp bin biblioteca.apresentacao.Main
```

O programa demonstrará as três etapas da arquitetura, gerando:
- Saída no console mostrando operações
- Arquivo `livros.csv` com dados de livros (Etapa 2)
- Arquivo `biblioteca.log` com histórico de eventos (Etapa 3)

## Arquitetura

### Etapa 1: Arquitetura em Camadas

A arquitetura em camadas organiza o sistema em camadas horizontais:

```
┌─────────────────────────────────────┐
│      CAMADA DE APRESENTAÇÃO         │
│       (biblioteca.apresentacao)     │
│            Main.java                │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│      CAMADA DE APLICAÇÃO            │
│         (biblioteca.servico)        │
│  LivroServico, UsuarioServico,      │
│      EmprestimoServico              │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│      CAMADA DE INFRAESTRUTURA       │
│    (biblioteca.infraestrutura.*)    │
│   Repositórios em Memória (HashMap) │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│      CAMADA DE DOMÍNIO              │
│        (biblioteca.dominio)         │
│   Livro, Usuario, Emprestimo        │
│   com regras de negócio             │
└─────────────────────────────────────┘
```

**Características:**
- Separação clara entre apresentação, serviços, infraestrutura e domínio
- Cada camada tem responsabilidades bem definidas
- Fácil de entender e manter para sistemas simples

**Desvantagem:**
- Alto acoplamento vertical entre camadas
- Mudanças em uma camada podem impactar todas as outras
- Difícil substituir implementações de baixo nível (ex: banco de dados)

### Etapa 2: Arquitetura Hexagonal (Ports and Adapters)

A arquitetura hexagonal (também conhecida como Clean Architecture ou Ports and Adapters) inverte o fluxo de dependências:

```
                    ┌─ ADAPTADOR: NotificacaoConsole
                    │
    ┌───────────────┼─────────────────┐
    │               │                 │
    │    ┌──────────▼──────────┐      │
┌───┴────┤  PORTA DE ENTRADA   │      │
│ CLI    │   PortaEmprestimo   │  CSV │
│ HTTP   └──────────┬──────────┘      │
└────────┬──────────┼──────────┬──────┘
         │          │          │
    ┌────▼──────────▼──────────▼──┐
    │                              │
    │   NÚCLEO DE NEGÓCIO         │
    │   (Domínio)                 │
    │                              │
    └────┬──────────┬──────────┬───┘
         │          │          │
┌────────┴──┐   ┌───▼────┐   ┌▼──────────────┐
│ PORTA DE  │   │PORTA DE│   │ PORTA DE      │
│ SAÍDA     │   │SAÍDA   │   │ SAÍDA         │
│Livro      │   │Usuario │   │Emprestimo     │
└────┬──────┘   └───┬────┘   └┬──────────────┘
     │              │         │
┌────▼──────┐  ┌────▼────┐  ┌▼──────────────┐
│ADAPTADOR: │  │ADAPTADOR│  │ADAPTADOR:     │
│Memória    │  │CSV      │  │Arquivo        │
│HashMap    │  │         │  │               │
└───────────┘  └─────────┘  └────────────────┘
```

**Características:**
- O domínio está no centro e não conhece nada sobre a infraestrutura
- As dependências apontam sempre para o centro (domínio)
- Fácil trocar adaptadores (ex: banco de dados, fonte de dados)
- Interface explícita via portas
- Sistema testável e flexível

**Vantagens:**
- Baixo acoplamento
- Fácil adicionar novos adaptadores
- Domínio independente de detalhes técnicos
- Segue princípios SOLID

### Etapa 3: Comunicação Assíncrona por Eventos (Publisher/Subscriber)

Implementação de um sistema de eventos desacoplado:

```
┌──────────────────────────────────────────────────────┐
│                  EmprestimoServico                   │
│            (Publicador de Eventos)                   │
│                                                      │
│  - Realiza empréstimo                               │
│  - Publica: EmprestimoRealizadoEvento               │
│  - Registra devolução                               │
│  - Publica: DevolucaoRegistradaEvento               │
└──────────────────┬───────────────────────────────────┘
                   │
                   │ Eventos publicados via EventBus
                   │
        ┌──────────┴──────────┐
        │                     │
        ▼                     ▼
┌──────────────────┐  ┌──────────────────┐
│ServicoDeNotif.   │  │ServicoDeLog      │
│(Consumidor)      │  │(Consumidor)      │
│                  │  │                  │
│- Escuta eventos  │  │- Escuta eventos  │
│- Envia notif.    │  │- Escreve no log  │
│- Para console    │  │- Arquivo         │
└──────────────────┘  └──────────────────┘
```

**Características:**
- EventBus desacoplado com genéricos
- Publishers não conhecem subscribers
- Fácil adicionar novos listeners
- Comunicação assíncrona e flexível

**Implementação:**
- `EventBus.java`: Gerenciador de eventos genérico
- `EmprestimoRealizadoEvento.java`: Evento publicado quando empréstimo é realizado
- `DevolucaoRegistradaEvento.java`: Evento publicado quando devolução é registrada
- `ServicoDeNotificacao.java`: Listener que envia notificações
- `ServicoDeLog.java`: Listener que registra eventos em arquivo

## Explicação da Arquitetura Hexagonal

### O que é?

A Arquitetura Hexagonal é um padrão arquitetural que coloca a lógica de negócio (domínio) no centro do sistema, isolado de detalhes técnicos. O domínio se comunica com o mundo externo através de **portas** e **adaptadores**.

### Portas

**Portas de Entrada** (Input Ports):
- Definem os casos de uso do sistema
- Interface que o mundo externo usa para interagir com o domínio
- Exemplo: `PortaEmprestimo` com métodos `realizarEmprestimo`, `registrarDevolucao`

**Portas de Saída** (Output Ports):
- Definem como o domínio se comunica com sistemas externos
- Exemplo: `PortaLivroRepositorio`, `PortaUsuarioRepositorio`, `PortaNotificacao`

### Adaptadores

**Adaptadores de Entrada** (Input Adapters):
- Traduzem solicitações externas para chamadas de portas de entrada
- Exemplo: CLI, HTTP, GUI

**Adaptadores de Saída** (Output Adapters):
- Implementam as portas de saída
- Exemplo: `LivroRepositorioMemoria`, `LivroRepositorioCsv`, `NotificacaoConsole`

### Benefícios

1. **Independência do Domínio**: A lógica de negócio não depende de frameworks ou tecnologias
2. **Testabilidade**: Fácil criar mocks e testes unitários
3. **Flexibilidade**: Trocar implementações sem alterar a lógica central
4. **Manutenibilidade**: Código organizado e com responsabilidades claras

## Sistema de Eventos

### EventBus

O `EventBus` implementa o padrão Publisher/Subscriber com genéricos:

```java
EventBus eventBus = new EventBus();

// Assinando eventos
eventBus.assinar(EmprestimoRealizadoEvento.class, 
    evento -> System.out.println("Emprestimo: " + evento));

// Publicando eventos
EmprestimoRealizadoEvento evento = new EmprestimoRealizadoEvento(...);
eventBus.publicar(evento);
```

### Desacoplamento

`EmprestimoServico` não importa:
- `ServicoDeNotificacao`
- `ServicoDeLog`

Todo acoplamento é feito apenas via `EventBus`. Isso permite:
- Adicionar novos listeners sem modificar `EmprestimoServico`
- Remover listeners sem afetar o serviço
- Testar `EmprestimoServico` isoladamente

## Estrutura de Diretórios

```
src/biblioteca/
├── dominio/                    # Lógica de negócio
│   ├── Livro.java
│   ├── Usuario.java
│   ├── Emprestimo.java
│   ├── SituacaoEmprestimo.java
│   ├── SituacaoUsuario.java
│   └── evento/
│       ├── EmprestimoRealizadoEvento.java
│       └── DevolucaoRegistradaEvento.java
├── porta/                      # Interfaces das portas
│   ├── entrada/
│   │   └── PortaEmprestimo.java
│   └── saida/
│       ├── PortaLivroRepositorio.java
│       ├── PortaUsuarioRepositorio.java
│       ├── PortaEmprestimoRepositorio.java
│       └── PortaNotificacao.java
├── servico/                    # Implementações de serviços
│   ├── LivroServico.java
│   ├── UsuarioServico.java
│   ├── EmprestimoServico.java
│   ├── EventBus.java
│   ├── ServicoDeNotificacao.java
│   └── ServicoDeLog.java
├── infraestrutura/             # Implementações técnicas
│   └── adaptador/
│       ├── LivroRepositorio.java (Etapa 1)
│       ├── UsuarioRepositorio.java (Etapa 1)
│       ├── EmprestimoRepositorio.java (Etapa 1)
│       ├── LivroRepositorioMemoria.java (Etapa 2)
│       ├── UsuarioRepositorioMemoria.java (Etapa 2)
│       ├── EmprestimoRepositorioMemoria.java (Etapa 2)
│       ├── LivroRepositorioCsv.java (Etapa 2)
│       └── NotificacaoConsole.java (Etapa 2)
└── apresentacao/               # Camada de apresentação
    └── Main.java
```

## Arquivos Gerados

- `livros.csv`: Dados de livros persistidos (Etapa 2)
- `biblioteca.log`: Registro de eventos (Etapa 3)
- `bin/`: Arquivos compilados

## Histórico de Commits

1. **Commit 1**: Criação da estrutura do projeto e entidades de domínio
   - Estrutura de pacotes
   - Entidades: Livro, Usuario, Emprestimo
   - Enums: SituacaoEmprestimo, SituacaoUsuario

2. **Commit 2**: Implementação da arquitetura em camadas e repositórios em memória
   - Repositórios simples em memória
   - Serviços (LivroServico, UsuarioServico, EmprestimoServico)
   - Main demonstrando Etapa 1

3. **Commit 3**: Criação das portas de entrada e saída para arquitetura hexagonal
   - Portas de entrada (PortaEmprestimo)
   - Portas de saída (PortaLivroRepositorio, etc.)
   - Adaptadores de memória
   - Adaptador CSV para livros
   - Adaptador de notificação

4. **Commit 4**: Refatoração para arquitetura hexagonal com portas
   - Implementação do EventBus
   - Records de eventos
   - Handlers de eventos
   - EmprestimoServico publicando eventos

5. **Commit 5**: Implementação dos adaptadores de infraestrutura e repositório CSV
   - (Adaptadores já incluídos no commit anterior)

6. **Commit 6**: Implementação do EventBus, eventos, handlers e finalização
   - (Já implementado nos commits anteriores)
   - Demonstração completa das 3 etapas na Main

## Dificuldades Encontradas

### 1. Compilação de Múltiplos Pacotes
**Dificuldade**: Compilar recursivamente todos os pacotes Java
**Solução**: Usar `javac` com padrões glob (*.java) para cada diretório

### 2. Encoding UTF-8 no Console
**Dificuldade**: Caracteres especiais apareciam mal no console Windows
**Impacto**: Apenas visual, o código funciona corretamente
**Observação**: Não afeta a funcionalidade do programa

### 3. Gerenciamento de Estado com Eventos
**Dificuldade**: Manter sincronizado o estado entre produtor e consumidor de eventos
**Solução**: Listeners têm acesso direto aos repositórios para buscar estado atual

### 4. Genéricos em Java
**Dificuldade**: Implementar EventBus com suporte a diferentes tipos de eventos
**Solução**: Usar `<T>` para genéricos e `@SuppressWarnings("unchecked")` onde necessário

### 5. Desacoplamento Total
**Dificuldade**: EmprestimoServico não deveria conhecer os listeners
**Solução**: EventBus injeta os listeners, não importações diretas

### 6. Persistência em CSV
**Dificuldade**: Sincronizar dados entre memória e arquivo
**Solução**: Carregar dados do arquivo na inicialização e salvar após cada modificação

## Tecnologias Utilizadas

- **Linguagem**: Java 17+
- **Paradigma**: Orientado a Objetos
- **Padrões**: 
  - Arquitetura Hexagonal
  - Publisher/Subscriber
  - Adapter Pattern
  - Repository Pattern
- **Libs**: Apenas Java Standard Library

## Conclusão

Este projeto demonstra como evoluir de uma arquitetura simples (em camadas) para uma mais sofisticada (hexagonal) com comunicação assíncrona. Cada etapa adiciona complexidade mas também flexibilidade e desacoplamento.

A Arquitetura Hexagonal é particularmente útil em sistemas que precisam:
- Ser altamente testáveis
- Ter múltiplas interfaces de entrada/saída
- Serem independentes de frameworks
- Serem fáceis de manter e estender

package biblioteca.apresentacao;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.infraestrutura.adaptador.*;
import biblioteca.porta.entrada.PortaEmprestimo;
import biblioteca.porta.saida.*;
import biblioteca.servico.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== SISTEMA DE GERENCIAMENTO DE BIBLIOTECA =====");
        System.out.println();

        // Etapa 1: Arquitetura em Camadas
        executarEtapa1();
        
        System.out.println();
        System.out.println("===================================================");
        System.out.println();

        // Etapa 2: Arquitetura Hexagonal com adaptadores
        executarEtapa2();

        System.out.println();
        System.out.println("===================================================");
        System.out.println();

        // Etapa 3: Comunicação Assíncrona por Eventos
        executarEtapa3();
    }

    private static void executarEtapa1() {
        System.out.println("--- ETAPA 1: ARQUITETURA EM CAMADAS ---");
        System.out.println();

        // Criando repositórios em memória da antiga forma
        LivroRepositorio livroRepositorio = new LivroRepositorio();
        UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
        EmprestimoRepositorio emprestimoRepositorio = new EmprestimoRepositorio();

        // Criando serviços
        LivroServico livroServico = new LivroServico(livroRepositorio);
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepositorio);
        EmprestimoServico emprestimoServico = new EmprestimoServico(emprestimoRepositorio, 
                                                                     livroRepositorio, 
                                                                     usuarioRepositorio);

        // 1. Cadastrando livros
        System.out.println("1. Cadastrando Livros:");
        livroServico.cadastrarLivro(1L, "Clean Code", "Robert C. Martin", "978-0132350884", 5);
        livroServico.cadastrarLivro(2L, "Design Patterns", "Gang of Four", "978-0201633610", 3);
        livroServico.cadastrarLivro(3L, "Refactoring", "Martin Fowler", "978-0201485677", 2);
        System.out.println("   ok 3 livros cadastrados");
        System.out.println();

        // 2. Cadastrando usuários
        System.out.println("2. Cadastrando Usuarios:");
        usuarioServico.cadastrarUsuario(1L, "Joao Silva", "joao@email.com");
        usuarioServico.cadastrarUsuario(2L, "Maria Santos", "maria@email.com");
        System.out.println("   ok 2 usuarios cadastrados");
        System.out.println();

        // 3. Realizando empréstimo
        System.out.println("3. Realizando Emprestimo:");
        Emprestimo emprestimo1 = emprestimoServico.realizarEmprestimo(1L, 1L);
        System.out.println("   ok " + emprestimo1);
        System.out.println("   Data prevista de devolucao: " + emprestimo1.getDataPrevistaDevolucao());
        System.out.println();

        Emprestimo emprestimo2 = emprestimoServico.realizarEmprestimo(2L, 2L);
        System.out.println("   ok " + emprestimo2);
        System.out.println();

        // 4. Listando empréstimos ativos
        System.out.println("4. Emprestimos Ativos:");
        List<Emprestimo> ativos = emprestimoServico.listarEmprestimosAtivos();
        ativos.forEach(e -> System.out.println("   - " + e));
        System.out.println();

        // 5. Registrando devolução
        System.out.println("5. Registrando Devolucao:");
        emprestimoServico.registrarDevolucao(1L);
        System.out.println("   ok Devolucao registrada para emprestimo 1");
        System.out.println();

        // 6. Listando empréstimos ativos após devolução
        System.out.println("6. Emprestimos Ativos apos Devolucao:");
        ativos = emprestimoServico.listarEmprestimosAtivos();
        if (ativos.isEmpty()) {
            System.out.println("   ok Nenhum emprestimo ativo");
        } else {
            ativos.forEach(e -> System.out.println("   - " + e));
        }
    }

    private static void executarEtapa2() {
        System.out.println("--- ETAPA 2: ARQUITETURA HEXAGONAL ---");
        System.out.println();

        System.out.println("A) Demonstrando com Adaptador em MEMORIA:");
        System.out.println();

        // Usando adaptadores de memória
        PortaLivroRepositorio livroRepositorioMemoria = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepositorioMemoria = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepositorioMemoria = new EmprestimoRepositorioMemoria();

        LivroServico livroServico1 = new LivroServico(livroRepositorioMemoria);
        UsuarioServico usuarioServico1 = new UsuarioServico(usuarioRepositorioMemoria);
        EmprestimoServico emprestimoServico1 = new EmprestimoServico(emprestimoRepositorioMemoria, 
                                                                      livroRepositorioMemoria, 
                                                                      usuarioRepositorioMemoria);

        livroServico1.cadastrarLivro(10L, "O Pragmatico", "David Thomas", "978-8577807742", 4);
        livroServico1.cadastrarLivro(11L, "Codigo Limpo", "Robert Martin", "978-8595086621", 2);
        usuarioServico1.cadastrarUsuario(10L, "Pedro Costa", "pedro@email.com");
        usuarioServico1.cadastrarUsuario(11L, "Ana Silva", "ana@email.com");

        Emprestimo emp1 = emprestimoServico1.realizarEmprestimo(10L, 10L);
        System.out.println("Emprestimo realizado: " + emp1);
        System.out.println();

        System.out.println("B) Demonstrando com Adaptador CSV:");
        System.out.println();

        // Usando adaptador CSV
        PortaLivroRepositorio livroRepositorioCsv = new LivroRepositorioCsv();
        PortaUsuarioRepositorio usuarioRepositorioMemoria2 = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepositorioMemoria2 = new EmprestimoRepositorioMemoria();

        LivroServico livroServico2 = new LivroServico(livroRepositorioCsv);
        UsuarioServico usuarioServico2 = new UsuarioServico(usuarioRepositorioMemoria2);
        EmprestimoServico emprestimoServico2 = new EmprestimoServico(emprestimoRepositorioMemoria2, 
                                                                      livroRepositorioCsv, 
                                                                      usuarioRepositorioMemoria2);

        livroServico2.cadastrarLivro(20L, "Padroes de Projeto", "Gang of Four", "978-8589151481", 3);
        livroServico2.cadastrarLivro(21L, "Test Driven Development", "Kent Beck", "978-8577801573", 1);
        System.out.println("Livros salvos em arquivo livros.csv");
        System.out.println();

        usuarioServico2.cadastrarUsuario(20L, "Lucas Santos", "lucas@email.com");
        Emprestimo emp2 = emprestimoServico2.realizarEmprestimo(20L, 20L);
        System.out.println("Emprestimo realizado com adaptador CSV: " + emp2);
        System.out.println();

        // Demonstrando adapter de notificação
        System.out.println("C) Demonstrando Adaptador de Notificacao:");
        System.out.println();
        
        PortaNotificacao notificacao = new NotificacaoConsole();
        notificacao.notificarAtraso(usuarioServico2.buscarUsuario(20L), emp2);
    }

    private static void executarEtapa3() {
        System.out.println("--- ETAPA 3: COMUNICACAO ASSINCRONA POR EVENTOS ---");
        System.out.println();

        // Criando EventBus
        EventBus eventBus = new EventBus();

        // Criando repositórios
        PortaLivroRepositorio livroRepositorio = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepositorio = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepositorio = new EmprestimoRepositorioMemoria();

        // Criando serviços
        LivroServico livroServico = new LivroServico(livroRepositorio);
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepositorio);
        EmprestimoServico emprestimoServico = new EmprestimoServico(emprestimoRepositorio,
                                                                      livroRepositorio,
                                                                      usuarioRepositorio,
                                                                      eventBus);

        // Criando handlers de eventos
        ServicoDeNotificacao servicoNotificacao = new ServicoDeNotificacao(usuarioRepositorio, emprestimoRepositorio);
        ServicoDeLog servicoLog = new ServicoDeLog();

        // Registrando subscribers no EventBus
        System.out.println("Registrando subscribers no EventBus...");
        eventBus.assinar(EmprestimoRealizadoEvento.class, servicoNotificacao::consumirEmprestimoRealizado);
        eventBus.assinar(EmprestimoRealizadoEvento.class, servicoLog::consumirEmprestimoRealizado);
        eventBus.assinar(DevolucaoRegistradaEvento.class, servicoLog::consumirDevolucaoRegistrada);
        System.out.println("ok 3 subscribers registrados");
        System.out.println();

        // Cadastrando dados
        System.out.println("Cadastrando dados de teste...");
        livroServico.cadastrarLivro(100L, "Algoritmos", "Cormen", "978-0262033848", 2);
        livroServico.cadastrarLivro(101L, "Estruturas de Dados", "Knuth", "978-0321635778", 1);
        usuarioServico.cadastrarUsuario(100L, "Carlos Oliveira", "carlos@email.com");
        usuarioServico.cadastrarUsuario(101L, "Fernanda Costa", "fernanda@email.com");
        System.out.println("ok Dados cadastrados");
        System.out.println();

        // Realizando empréstimos - vai gerar eventos
        System.out.println("Realizando emprestimos (vai gerar eventos)...");
        System.out.println();

        Emprestimo emp1 = emprestimoServico.realizarEmprestimo(100L, 100L);
        System.out.println("Emprestimo 1 criado: " + emp1.getId());
        System.out.println();

        Emprestimo emp2 = emprestimoServico.realizarEmprestimo(101L, 101L);
        System.out.println("Emprestimo 2 criado: " + emp2.getId());
        System.out.println();

        // Registrando devoluções - vai gerar eventos
        System.out.println("Registrando devolucoes (vai gerar eventos)...");
        System.out.println();
        emprestimoServico.registrarDevolucao(1L);
        System.out.println("Devolucao 1 registrada");
        System.out.println();

        emprestimoServico.registrarDevolucao(2L);
        System.out.println("Devolucao 2 registrada");
        System.out.println();

        // Verificando arquivo de log
        System.out.println("Eventos foram registrados em arquivo biblioteca.log");
        System.out.println("Desacoplamento: EmprestimoServico nao conhece ServicoDeNotificacao ou ServicoDeLog");
        System.out.println("Toda comunicacao ocorre apenas via EventBus");
    }
}

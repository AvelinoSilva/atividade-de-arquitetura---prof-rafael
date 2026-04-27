package biblioteca.apresentacao;

import biblioteca.dominio.Emprestimo;
import biblioteca.infraestrutura.adaptador.LivroRepositorio;
import biblioteca.infraestrutura.adaptador.UsuarioRepositorio;
import biblioteca.infraestrutura.adaptador.EmprestimoRepositorio;
import biblioteca.servico.LivroServico;
import biblioteca.servico.UsuarioServico;
import biblioteca.servico.EmprestimoServico;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== SISTEMA DE GERENCIAMENTO DE BIBLIOTECA =====");
        System.out.println();

        // Etapa 1: Arquitetura em Camadas
        executarEtapa1();
    }

    private static void executarEtapa1() {
        System.out.println("--- ETAPA 1: ARQUITETURA EM CAMADAS ---");
        System.out.println();

        // Criando repositórios em memória
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
        System.out.println("   ✓ 3 livros cadastrados");
        System.out.println();

        // 2. Cadastrando usuários
        System.out.println("2. Cadastrando Usuários:");
        usuarioServico.cadastrarUsuario(1L, "João Silva", "joao@email.com");
        usuarioServico.cadastrarUsuario(2L, "Maria Santos", "maria@email.com");
        System.out.println("   ✓ 2 usuários cadastrados");
        System.out.println();

        // 3. Realizando empréstimo
        System.out.println("3. Realizando Empréstimo:");
        Emprestimo emprestimo1 = emprestimoServico.realizarEmprestimo(1L, 1L);
        System.out.println("   ✓ " + emprestimo1);
        System.out.println("   Data prevista de devolução: " + emprestimo1.getDataPrevistaDevolucao());
        System.out.println();

        Emprestimo emprestimo2 = emprestimoServico.realizarEmprestimo(2L, 2L);
        System.out.println("   ✓ " + emprestimo2);
        System.out.println();

        // 4. Listando empréstimos ativos
        System.out.println("4. Empréstimos Ativos:");
        List<Emprestimo> ativos = emprestimoServico.listarEmprestimosAtivos();
        ativos.forEach(e -> System.out.println("   - " + e));
        System.out.println();

        // 5. Registrando devolução
        System.out.println("5. Registrando Devolução:");
        emprestimoServico.registrarDevolucao(1L);
        System.out.println("   ✓ Devolução registrada para empréstimo 1");
        System.out.println();

        // 6. Listando empréstimos ativos após devolução
        System.out.println("6. Empréstimos Ativos após Devolução:");
        ativos = emprestimoServico.listarEmprestimosAtivos();
        if (ativos.isEmpty()) {
            System.out.println("   ✓ Nenhum empréstimo ativo");
        } else {
            ativos.forEach(e -> System.out.println("   - " + e));
        }
        System.out.println();

        // 7. Listando todos os livros
        System.out.println("7. Livros Disponíveis:");
        livroServico.listarLivros().forEach(l -> System.out.println("   - " + l));
    }
}

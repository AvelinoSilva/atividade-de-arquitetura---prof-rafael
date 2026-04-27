package biblioteca.servico;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.adaptador.EmprestimoRepositorio;
import biblioteca.infraestrutura.adaptador.LivroRepositorio;
import biblioteca.infraestrutura.adaptador.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmprestimoServico {
    private EmprestimoRepositorio emprestimoRepositorio;
    private LivroRepositorio livroRepositorio;
    private UsuarioRepositorio usuarioRepositorio;
    private Long proximoId = 1L;

    public EmprestimoServico(EmprestimoRepositorio emprestimoRepositorio, 
                            LivroRepositorio livroRepositorio, 
                            UsuarioRepositorio usuarioRepositorio) {
        this.emprestimoRepositorio = emprestimoRepositorio;
        this.livroRepositorio = livroRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId) {
        Usuario usuario = usuarioRepositorio.buscarPorId(usuarioId);
        Livro livro = livroRepositorio.buscarPorId(livroId);

        if (usuario == null || livro == null) {
            throw new IllegalArgumentException("Usuario ou Livro nao encontrado");
        }

        if (!livro.realizarEmprestimo()) {
            throw new IllegalStateException("Livro nao disponivel para emprestimo");
        }

        LocalDate dataRetirada = LocalDate.now();
        LocalDate dataPrevistaDevolucao = dataRetirada.plusDays(14);

        Emprestimo emprestimo = new Emprestimo(proximoId++, livro, usuario, dataRetirada, 
                                               dataPrevistaDevolucao, SituacaoEmprestimo.ATIVO);
        emprestimoRepositorio.salvar(emprestimo);

        return emprestimo;
    }

    public void registrarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepositorio.buscarPorId(emprestimoId);

        if (emprestimo == null) {
            throw new IllegalArgumentException("Emprestimo nao encontrado");
        }

        emprestimo.getLivro().registrarDevolucao();
        emprestimo.setSituacao(SituacaoEmprestimo.DEVOLVIDO);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepositorio.listarTodos().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.ATIVO)
                .collect(Collectors.toList());
    }

    public List<Emprestimo> verificarAtrasos() {
        return emprestimoRepositorio.listarTodos().stream()
                .filter(Emprestimo::estaAtrasado)
                .peek(e -> e.setSituacao(SituacaoEmprestimo.ATRASADO))
                .collect(Collectors.toList());
    }
}

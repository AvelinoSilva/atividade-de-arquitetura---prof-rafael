package biblioteca.porta.saida;

import biblioteca.dominio.Emprestimo;
import java.util.List;

public interface PortaEmprestimoRepositorio {
    void salvar(Emprestimo emprestimo);
    Emprestimo buscarPorId(Long id);
    List<Emprestimo> listarTodos();
    void remover(Long id);
}

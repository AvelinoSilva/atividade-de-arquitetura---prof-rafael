package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Emprestimo;
import biblioteca.porta.saida.PortaEmprestimoRepositorio;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmprestimoRepositorioMemoria implements PortaEmprestimoRepositorio {
    private Map<Long, Emprestimo> emprestimos = new HashMap<>();

    @Override
    public void salvar(Emprestimo emprestimo) {
        emprestimos.put(emprestimo.getId(), emprestimo);
    }

    @Override
    public Emprestimo buscarPorId(Long id) {
        return emprestimos.get(id);
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new java.util.ArrayList<>(emprestimos.values());
    }

    @Override
    public void remover(Long id) {
        emprestimos.remove(id);
    }
}

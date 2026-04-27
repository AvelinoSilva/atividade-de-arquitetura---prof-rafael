package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Emprestimo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmprestimoRepositorio {
    private Map<Long, Emprestimo> emprestimos = new HashMap<>();

    public void salvar(Emprestimo emprestimo) {
        emprestimos.put(emprestimo.getId(), emprestimo);
    }

    public Emprestimo buscarPorId(Long id) {
        return emprestimos.get(id);
    }

    public List<Emprestimo> listarTodos() {
        return new java.util.ArrayList<>(emprestimos.values());
    }

    public void remover(Long id) {
        emprestimos.remove(id);
    }
}

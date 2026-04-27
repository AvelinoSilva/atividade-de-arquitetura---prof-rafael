package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LivroRepositorio {
    private Map<Long, Livro> livros = new HashMap<>();

    public void salvar(Livro livro) {
        livros.put(livro.getId(), livro);
    }

    public Livro buscarPorId(Long id) {
        return livros.get(id);
    }

    public List<Livro> listarTodos() {
        return new java.util.ArrayList<>(livros.values());
    }

    public void remover(Long id) {
        livros.remove(id);
    }
}

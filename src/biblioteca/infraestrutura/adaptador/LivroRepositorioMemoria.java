package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.porta.saida.PortaLivroRepositorio;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LivroRepositorioMemoria implements PortaLivroRepositorio {
    private Map<Long, Livro> livros = new HashMap<>();

    @Override
    public void salvar(Livro livro) {
        livros.put(livro.getId(), livro);
    }

    @Override
    public Livro buscarPorId(Long id) {
        return livros.get(id);
    }

    @Override
    public List<Livro> listarTodos() {
        return new java.util.ArrayList<>(livros.values());
    }

    @Override
    public void remover(Long id) {
        livros.remove(id);
    }
}

package biblioteca.porta.saida;

import biblioteca.dominio.Livro;
import java.util.List;

public interface PortaLivroRepositorio {
    void salvar(Livro livro);
    Livro buscarPorId(Long id);
    List<Livro> listarTodos();
    void remover(Long id);
}

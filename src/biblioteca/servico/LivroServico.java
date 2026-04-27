package biblioteca.servico;

import biblioteca.dominio.Livro;
import biblioteca.infraestrutura.adaptador.LivroRepositorio;
import java.util.List;

public class LivroServico {
    private LivroRepositorio livroRepositorio;

    public LivroServico(LivroRepositorio livroRepositorio) {
        this.livroRepositorio = livroRepositorio;
    }

    public void cadastrarLivro(Long id, String titulo, String autor, String isbn, int quantidade) {
        Livro livro = new Livro(id, titulo, autor, isbn, quantidade);
        livroRepositorio.salvar(livro);
    }

    public Livro buscarLivro(Long id) {
        return livroRepositorio.buscarPorId(id);
    }

    public List<Livro> listarLivros() {
        return livroRepositorio.listarTodos();
    }

    public void removerLivro(Long id) {
        livroRepositorio.remover(id);
    }
}

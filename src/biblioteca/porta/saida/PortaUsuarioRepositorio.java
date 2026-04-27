package biblioteca.porta.saida;

import biblioteca.dominio.Usuario;
import java.util.List;

public interface PortaUsuarioRepositorio {
    void salvar(Usuario usuario);
    Usuario buscarPorId(Long id);
    List<Usuario> listarTodos();
    void remover(Long id);
}

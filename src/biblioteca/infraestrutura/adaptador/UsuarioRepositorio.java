package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioRepositorio {
    private Map<Long, Usuario> usuarios = new HashMap<>();

    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }

    public List<Usuario> listarTodos() {
        return new java.util.ArrayList<>(usuarios.values());
    }

    public void remover(Long id) {
        usuarios.remove(id);
    }
}

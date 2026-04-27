package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;
import biblioteca.porta.saida.PortaUsuarioRepositorio;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioRepositorioMemoria implements PortaUsuarioRepositorio {
    private Map<Long, Usuario> usuarios = new HashMap<>();

    @Override
    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }

    @Override
    public List<Usuario> listarTodos() {
        return new java.util.ArrayList<>(usuarios.values());
    }

    @Override
    public void remover(Long id) {
        usuarios.remove(id);
    }
}

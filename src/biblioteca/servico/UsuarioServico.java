package biblioteca.servico;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.infraestrutura.adaptador.UsuarioRepositorio;
import java.util.List;

public class UsuarioServico {
    private UsuarioRepositorio usuarioRepositorio;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public void cadastrarUsuario(Long id, String nome, String email) {
        Usuario usuario = new Usuario(id, nome, email, SituacaoUsuario.ATIVO);
        usuarioRepositorio.salvar(usuario);
    }

    public Usuario buscarUsuario(Long id) {
        return usuarioRepositorio.buscarPorId(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.listarTodos();
    }

    public void removerUsuario(Long id) {
        usuarioRepositorio.remover(id);
    }
}

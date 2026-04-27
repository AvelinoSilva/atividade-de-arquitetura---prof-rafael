package biblioteca.servico;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.infraestrutura.adaptador.EmprestimoRepositorioMemoria;
import biblioteca.infraestrutura.adaptador.UsuarioRepositorioMemoria;
import biblioteca.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.porta.saida.PortaUsuarioRepositorio;

public class ServicoDeNotificacao {
    private PortaUsuarioRepositorio usuarioRepositorio;
    private PortaEmprestimoRepositorio emprestimoRepositorio;

    public ServicoDeNotificacao(PortaUsuarioRepositorio usuarioRepositorio,
                                PortaEmprestimoRepositorio emprestimoRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.emprestimoRepositorio = emprestimoRepositorio;
    }

    public void consumirEmprestimoRealizado(EmprestimoRealizadoEvento evento) {
        Usuario usuario = usuarioRepositorio.buscarPorId(evento.usuarioId());
        Emprestimo emprestimo = emprestimoRepositorio.buscarPorId(evento.emprestimoId());

        if (usuario != null && emprestimo != null) {
            System.out.println("[NOTIFICACAO] Novo Emprestimo Realizado!");
            System.out.println("  Usuario: " + usuario.getNome());
            System.out.println("  Email: " + usuario.getEmail());
            System.out.println("  Livro: " + emprestimo.getLivro().getTitulo());
            System.out.println("  Data de retirada: " + evento.dataRetirada());
            System.out.println("  Data prevista de devolucao: " + emprestimo.getDataPrevistaDevolucao());
            System.out.println();
        }
    }
}

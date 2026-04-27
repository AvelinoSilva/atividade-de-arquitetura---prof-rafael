package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.Emprestimo;
import biblioteca.porta.saida.PortaNotificacao;

public class NotificacaoConsole implements PortaNotificacao {
    @Override
    public void notificarAtraso(Usuario usuario, Emprestimo emprestimo) {
        System.out.println("[NOTIFICAÇÃO] Empréstimo Atrasado!");
        System.out.println("  Usuario: " + usuario.getNome());
        System.out.println("  Email: " + usuario.getEmail());
        System.out.println("  Livro: " + emprestimo.getLivro().getTitulo());
        System.out.println("  Data prevista de devolução: " + emprestimo.getDataPrevistaDevolucao());
    }
}

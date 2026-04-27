package biblioteca.porta.saida;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.Emprestimo;

public interface PortaNotificacao {
    void notificarAtraso(Usuario usuario, Emprestimo emprestimo);
}

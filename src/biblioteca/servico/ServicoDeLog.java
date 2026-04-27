package biblioteca.servico;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicoDeLog {
    private static final String ARQUIVO_LOG = "biblioteca.log";
    private static final DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void consumirEmprestimoRealizado(EmprestimoRealizadoEvento evento) {
        String timestamp = LocalDateTime.now().format(formatador);
        String mensagem = timestamp + " - Emprestimo realizado (ID: " + evento.emprestimoId() + 
                         ", Usuario ID: " + evento.usuarioId() + ", Livro ID: " + evento.livroId() + ")";
        
        registrarLog(mensagem);
    }

    public void consumirDevolucaoRegistrada(DevolucaoRegistradaEvento evento) {
        String timestamp = LocalDateTime.now().format(formatador);
        String statusAtraso = evento.comAtraso() ? "COM ATRASO" : "NO PRAZO";
        String mensagem = timestamp + " - Devolucao registrada (ID: " + evento.emprestimoId() + 
                         ", Status: " + statusAtraso + ")";
        
        registrarLog(mensagem);
    }

    private void registrarLog(String mensagem) {
        try (FileWriter writer = new FileWriter(ARQUIVO_LOG, true)) {
            writer.write(mensagem + "\n");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }
}

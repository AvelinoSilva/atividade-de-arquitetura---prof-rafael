package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.porta.saida.PortaLivroRepositorio;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LivroRepositorioCsv implements PortaLivroRepositorio {
    private static final String ARQUIVO_CSV = "livros.csv";
    private Map<Long, Livro> livros = new HashMap<>();

    public LivroRepositorioCsv() {
        carregarDoArquivo();
    }

    @Override
    public void salvar(Livro livro) {
        livros.put(livro.getId(), livro);
        salvarNoArquivo();
    }

    @Override
    public Livro buscarPorId(Long id) {
        return livros.get(id);
    }

    @Override
    public List<Livro> listarTodos() {
        return new java.util.ArrayList<>(livros.values());
    }

    @Override
    public void remover(Long id) {
        livros.remove(id);
        salvarNoArquivo();
    }

    private void salvarNoArquivo() {
        try (FileWriter writer = new FileWriter(ARQUIVO_CSV)) {
            writer.write("id,titulo,autor,isbn,quantidadeDisponivel\n");
            for (Livro livro : livros.values()) {
                writer.write(livro.getId() + "," + 
                           livro.getTitulo() + "," + 
                           livro.getAutor() + "," + 
                           livro.getIsbn() + "," + 
                           livro.getQuantidadeDisponivel() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar livros em CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(",");
                if (campos.length == 5) {
                    Long id = Long.parseLong(campos[0]);
                    String titulo = campos[1];
                    String autor = campos[2];
                    String isbn = campos[3];
                    int quantidade = Integer.parseInt(campos[4]);

                    Livro livro = new Livro(id, titulo, autor, isbn, quantidade);
                    livros.put(id, livro);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar livros de CSV: " + e.getMessage());
        }
    }
}

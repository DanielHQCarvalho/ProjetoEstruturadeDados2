import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Integra a Trie, responsavel pelos prefixos, com a BST, responsavel pelo ranking.
public class Dicionario {
    // Estruturas que permanecem sincronizadas durante todo o uso do programa.
    private final Trie trie;
    private final BSTWordRanking ranking;

    // Cria um dicionario inicialmente vazio.
    public Dicionario() {
        trie = new Trie();
        ranking = new BSTWordRanking();
    }

    // Le um arquivo com uma palavra por linha e retorna quantas palavras novas foram carregadas.
    public int carregar(String arquivo) throws IOException {
        int inserted = 0;
        // O try-with-resources fecha automaticamente o arquivo ao final da leitura.
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String line;
            // Le todas as linhas ate chegar ao fim do arquivo.
            while ((line = reader.readLine()) != null) {
                String word = line.trim();
                // Ignora linhas vazias e impede duplicar uma palavra ja existente.
                if (!word.isEmpty() && !trie.search(word)) {
                    adicionar(word);
                    inserted++;
                }
            }
        }
        return inserted;
    }

    // Adiciona uma palavra com frequencia inicial zero nas duas estruturas.
    public void adicionar(String word) {
        String normalized = normalize(word);
        // So insere quando a palavra ainda nao faz parte do dicionario.
        if (!trie.search(normalized)) {
            trie.insert(normalized);
            ranking.addOrUpdate(normalized, 0);
        }
    }

    // Delega a busca exata para a Trie.
    public boolean buscar(String word) {
        return trie.search(word);
    }

    // Delega a verificacao de prefixo para a Trie.
    public boolean buscarPrefixo(String prefix) {
        return trie.startsWith(prefix);
    }

    // Primeiro a Trie filtra pelo prefixo; depois a BST ordena pela frequencia.
    public List<String> sugerir(String prefix) {
        return ranking.sortCandidatesByFrequency(trie.suggest(prefix));
    }

    // Registra a escolha de uma palavra e mantem Trie e BST com a mesma frequencia.
    public void registrarUso(String word) {
        String normalized = normalize(word);
        // Uma palavra ainda inexistente entra no dicionario antes de receber o primeiro uso.
        if (!trie.search(normalized)) {
            adicionar(normalized);
        }
        // A Trie e a fonte de verdade da frequencia atual.
        int newFrequency = trie.getFrequency(normalized) + 1;
        trie.updateFrequency(normalized, newFrequency);
        ranking.addOrUpdate(normalized, newFrequency);
    }

    // Consulta a frequencia diretamente no no final da Trie.
    public int getFrequencia(String word) {
        return trie.getFrequency(word);
    }

    // Delega a montagem do Top-N para a BST.
    public List<String> getTopPalavras(int quantity) {
        return ranking.getTopWords(quantity);
    }

    // Retorna o simbolo da raiz, usado somente para demonstracao e testes.
    public String getRaiz() {
        return trie.getRootData();
    }

    // Padroniza maiusculas e espacos antes de enviar a palavra para as estruturas.
    private String normalize(String text) {
        // Evita chamar trim em uma referencia nula.
        if (text == null) {
            throw new IllegalArgumentException("A palavra nao pode ser nula.");
        }
        return text.trim().toLowerCase(Locale.ROOT);
    }
}

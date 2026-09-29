import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Armazena palavras em uma arvore de prefixos e oferece buscas e sugestoes.
public class Trie {
    // A raiz e representada por "*" e nao faz parte das palavras.
    private final TrieNode root;

    // Cria a Trie com uma raiz fixa. A raiz nao representa uma letra de palavra.
    public Trie() {
        root = new TrieNode("*");
    }

    // Insere uma palavra, criando somente os nos que ainda nao existem.
    public void insert(String word) {
        String normalized = normalize(word);
        TrieNode current = root;

        // Percorre a palavra letra por letra, sempre a partir da raiz ou do no anterior.
        for (int i = 0; i < normalized.length(); i++) {
            char letter = normalized.charAt(i);
            int index = letter - 'a';
            // Cria o filho quando esta letra ainda nao possui caminho na arvore.
            if (current.children[index] == null) {
                current.children[index] = new TrieNode(String.valueOf(letter));
            }
            current = current.children[index];
        }
        // O ultimo no visitado marca o termino da palavra inserida.
        current.isEndOfWord = true;
    }

    // Verifica se a palavra inteira existe; um simples prefixo nao e considerado palavra.
    public boolean search(String word) {
        TrieNode node = traverseTo(word);
        return node != null && node.isEndOfWord;
    }

    // Verifica apenas se existe um caminho para o prefixo informado.
    public boolean startsWith(String prefix) {
        return traverseTo(prefix) != null;
    }

    // Retorna todas as palavras que comecam com o prefixo, em ordem alfabetica.
    public List<String> suggest(String prefix) {
        String normalized = normalize(prefix);
        List<String> suggestions = new ArrayList<String>();
        TrieNode node = traverseTo(normalized);

        // Se o prefixo existe, percorre toda a subarvore a partir dele.
        if (node != null) {
            collectWords(node, new StringBuilder(normalized), suggestions);
        }
        return suggestions;
    }

    // Le a frequencia da palavra. Palavras ausentes possuem frequencia zero.
    public int getFrequency(String word) {
        TrieNode node = traverseTo(word);
        return node != null && node.isEndOfWord ? node.frequency : 0;
    }

    // Atualiza a frequencia guardada no no final de uma palavra existente.
    public void updateFrequency(String word, int frequency) {
        // Frequencia negativa nao e valida.
        if (frequency < 0) {
            throw new IllegalArgumentException("A frequencia nao pode ser negativa.");
        }
        TrieNode node = traverseTo(word);
        // Impede atualizar uma palavra que nao foi inserida.
        if (node == null || !node.isEndOfWord) {
            throw new IllegalArgumentException("Palavra nao encontrada: " + word);
        }
        node.frequency = frequency;
    }

    // Exibe o valor simbolico da raiz para demonstracao ou testes.
    public String getRootData() {
        return root.getData();
    }

    // Caminha pela arvore ate o ultimo caractere do texto e devolve o no encontrado.
    private TrieNode traverseTo(String text) {
        String normalized = normalize(text);
        TrieNode current = root;

        // Cada letra aponta para uma posicao especifica no vetor de filhos.
        for (int i = 0; i < normalized.length(); i++) {
            int index = normalized.charAt(i) - 'a';
            // Sem filho para a letra atual significa que o caminho nao existe.
            if (current.children[index] == null) {
                return null;
            }
            current = current.children[index];
        }
        return current;
    }

    // Faz uma busca em profundidade (DFS) para montar todas as palavras abaixo de um no.
    private void collectWords(TrieNode node, StringBuilder word, List<String> suggestions) {
        // Sempre que encontra um fim de palavra, registra a palavra montada ate aqui.
        if (node.isEndOfWord) {
            suggestions.add(word.toString());
        }
        // Visitar os filhos de a a z garante que a lista final fique alfabetica.
        for (int i = 0; i < TrieNode.ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                // Adiciona a letra, explora o filho e remove a letra ao voltar (backtracking).
                word.append((char) ('a' + i));
                collectWords(node.children[i], word, suggestions);
                word.deleteCharAt(word.length() - 1);
            }
        }
    }

    // Padroniza a entrada e rejeita palavras fora do alfabeto permitido.
    private String normalize(String text) {
        // Uma referencia nula nao pode ser pesquisada nem inserida.
        if (text == null) {
            throw new IllegalArgumentException("A palavra nao pode ser nula.");
        }
        String normalized = text.trim().toLowerCase(Locale.ROOT);
        // Depois de remover espacos, ainda deve restar pelo menos uma letra.
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("A palavra nao pode ser vazia.");
        }
        // A implementacao usa somente as letras minusculas de a ate z.
        for (int i = 0; i < normalized.length(); i++) {
            char letter = normalized.charAt(i);
            if (letter < 'a' || letter > 'z') {
                throw new IllegalArgumentException("Use somente letras de a a z.");
            }
        }
        return normalized;
    }
}

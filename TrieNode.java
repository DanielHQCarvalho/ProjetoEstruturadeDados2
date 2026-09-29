// Representa um unico no da Trie. Cada no guarda uma letra e seus filhos.
public class TrieNode {
    // Quantidade de letras do alfabeto ingles: de a ate z.
    public static final int ALPHABET_SIZE = 26;

    // Letra que o no representa. A raiz usa o simbolo "*".
    private final String data;
    // Vetor de filhos: indice 0 representa a, indice 1 representa b, e assim por diante.
    final TrieNode[] children;
    // Indica se o caminho ate este no forma uma palavra completa.
    boolean isEndOfWord;
    // Quantidade de vezes que a palavra deste no final foi escolhida.
    int frequency;

    // Cria um no sem filhos, sem fim de palavra e com frequencia inicial zero.
    public TrieNode(String data) {
        this.data = data;
        this.children = new TrieNode[ALPHABET_SIZE];
        this.isEndOfWord = false;
        this.frequency = 0;
    }

    // Permite consultar a letra armazenada no no.
    public String getData() {
        return data;
    }
}

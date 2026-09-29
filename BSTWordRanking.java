//Integrantes do grupo
//Nome: Daniel Henrique Carvalho RA:10736657
//Nome: Felipe Sousa Vasques Honorato RA:10735577

import java.util.ArrayList;
import java.util.List;

// Mantem palavras em uma BST e permite gerar rankings por frequencia de uso.
public class BSTWordRanking {
    // Raiz da arvore binaria de busca.
    private Node root;

    // No interno: a BST e organizada pela palavra, e nao pela frequencia.
    private static class Node {
        // Palavra e total de usos armazenados no no.
        String word;
        int frequency;
        // Filhos esquerdo e direito da BST.
        Node left;
        Node right;

        // Cria um no completo com palavra e frequencia inicial.
        Node(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }
    }

    // Insere uma palavra nova ou atualiza a frequencia caso ela ja exista.
    public void addOrUpdate(String word, int frequency) {
        if (word == null || word.trim().isEmpty() || frequency < 0) {
            throw new IllegalArgumentException("Palavra ou frequencia invalida.");
        }
        // A raiz pode mudar quando a primeira palavra e inserida.
        root = addOrUpdate(root, word, frequency);
    }

    // Busca uma palavra na BST e devolve zero quando ela nao esta presente.
    public int getFrequency(String word) {
        Node node = find(root, word);
        return node == null ? 0 : node.frequency;
    }

    // Retorna, da mais usada para a menos usada, no maximo a quantidade solicitada.
    public List<String> getTopWords(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("A quantidade nao pode ser negativa.");
        }
        // Primeiro coleta todos os nos da BST e depois ordena essa lista por frequencia.
        List<Node> nodes = new ArrayList<Node>();
        collectAll(root, nodes);
        sortByFrequency(nodes);

        List<String> words = new ArrayList<String>();
        // Copia para a resposta somente as palavras que cabem no Top-N.
        for (int i = 0; i < quantity && i < nodes.size(); i++) {
            words.add(nodes.get(i).word);
        }
        return words;
    }

    // Ordena apenas as sugestoes que ja foram filtradas pela Trie para um prefixo.
    public List<String> sortCandidatesByFrequency(List<String> candidates) {
        List<String> ordered = new ArrayList<String>(candidates);
        // Insertion sort: cada palavra e inserida na posicao correta da lista.
        for (int i = 1; i < ordered.size(); i++) {
            String current = ordered.get(i);
            int j = i - 1;

            while (j >= 0 && comesBefore(current, ordered.get(j))) {
                ordered.set(j + 1, ordered.get(j));
                j--;
            }
            ordered.set(j + 1, current);
        }
        return ordered;
    }

    // Percorre recursivamente a BST usando a comparacao lexicografica das palavras.
    private Node addOrUpdate(Node node, String word, int frequency) {
        // A primeira posicao vazia encontrada recebe o novo no.
        if (node == null) {
            return new Node(word, frequency);
        }
        int comparison = word.compareTo(node.word);
        // Palavras menores ficam a esquerda; maiores ficam a direita.
        if (comparison < 0) {
            node.left = addOrUpdate(node.left, word, frequency);
        } else if (comparison > 0) {
            node.right = addOrUpdate(node.right, word, frequency);
        } else {
            // Mesma palavra: apenas substitui a frequencia armazenada.
            node.frequency = frequency;
        }
        return node;
    }

    // Localiza um no sem percorrer ramos que nao podem conter a palavra buscada.
    private Node find(Node node, String word) {
        while (node != null) {
            int comparison = word.compareTo(node.word);
            // Comparacao zero significa que a palavra foi encontrada.
            if (comparison == 0) {
                return node;
            }
            node = comparison < 0 ? node.left : node.right;
        }
        return null;
    }

    // Percurso em ordem que transfere todos os nos da BST para uma lista auxiliar.
    private void collectAll(Node node, List<Node> nodes) {
        if (node == null) {
            return;
        }
        // Esquerda, no atual e direita produzem palavras em ordem alfabetica.
        collectAll(node.left, nodes);
        nodes.add(node);
        collectAll(node.right, nodes);
    }

    // Ordena a lista final por frequencia usando insertion sort.
    private void sortByFrequency(List<Node> nodes) {
        // Percorre a lista e coloca cada no na sua posicao correta.
        for (int i = 1; i < nodes.size(); i++) {
            Node current = nodes.get(i);
            int j = i - 1;

            while (j >= 0 && comesBefore(current, nodes.get(j))) {
                nodes.set(j + 1, nodes.get(j));
                j--;
            }
            nodes.set(j + 1, current);
        }
    }

    // Decide a ordem de duas palavras: maior frequencia primeiro e, em empate, ordem alfabetica.
    private boolean comesBefore(String first, String second) {
        int firstFrequency = getFrequency(first);
        int secondFrequency = getFrequency(second);

        if (firstFrequency != secondFrequency) {
            return firstFrequency > secondFrequency;
        }
        return first.compareTo(second) < 0;
    }

    // Versao da comparacao acima para os nos completos usados no ranking global.
    private boolean comesBefore(Node first, Node second) {
        if (first.frequency != second.frequency) {
            return first.frequency > second.frequency;
        }
        return first.word.compareTo(second.word) < 0;
    }
}

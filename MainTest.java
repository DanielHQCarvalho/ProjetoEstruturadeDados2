import java.util.Arrays;

// Suite simples de testes sem bibliotecas externas, conforme pedido no enunciado.
public class MainTest {
    // Contadores usados para exibir o resumo final da execucao.
    private static int passed;
    private static int total;

    // Executa os testes da Trie, da BST e da integracao entre as duas estruturas.
    public static void main(String[] args) {
        // Cria um dicionario novo e verifica a representacao da raiz da Trie.
        Dicionario dic = new Dicionario();
        assertEquals("Raiz da Trie", "*", dic.getRaiz());

        // Prepara palavras com prefixos compartilhados para testar a Trie.
        dic.adicionar("casa");
        dic.adicionar("carro");
        dic.adicionar("cachorro");
        dic.adicionar("cadeira");
        dic.adicionar("computador");
        dic.adicionar("compra");

        // Testes da Fase 1: busca exata, busca por prefixo e sugestoes alfabeticas.
        assertTrue("Busca de casa", dic.buscar("casa"));
        assertTrue("Busca de palavra ausente", !dic.buscar("xyz"));
        assertTrue("Busca por prefixo", dic.buscarPrefixo("ca"));
        assertEquals("Sugestoes alfabeticas", Arrays.asList("cachorro", "cadeira", "carro", "casa"),
                dic.sugerir("ca"));

        // Testes da Fase 2: atualizacao de frequencia e ordenacao do ranking.
        dic.registrarUso("computador");
        dic.registrarUso("computador");
        dic.registrarUso("compra");
        assertEquals("Frequencia de computador", 2, dic.getFrequencia("computador"));
        assertEquals("Sugestoes por frequencia", Arrays.asList("computador", "compra"), dic.sugerir("comp"));
        assertEquals("Ranking geral", Arrays.asList("computador", "compra", "cachorro"),
                dic.getTopPalavras(3));

        System.out.println("\n=== RESULTADO: " + passed + "/" + total + " testes PASSED ===");
        // Retorna erro ao sistema se pelo menos uma verificacao falhar.
        if (passed != total) {
            System.exit(1);
        }
    }

    // Registra o resultado de um teste booleano e imprime PASSED ou FAILED.
    private static void assertTrue(String name, boolean result) {
        total++;
        // Atualiza os contadores de acordo com o resultado recebido.
        if (result) {
            passed++;
            System.out.println("[PASSED] " + name);
        } else {
            System.out.println("[FAILED] " + name);
        }
    }

    // Compara objetos usando equals e reaproveita a rotina de teste booleano.
    private static void assertEquals(String name, Object expected, Object actual) {
        assertTrue(name, expected.equals(actual));
    }
}

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Interface de console para testar as funcionalidades das fases 1 e 2.
public class Main {
    // Inicia o dicionario, carrega o arquivo e mantem o menu ativo ate a opcao de sair.
    public static void main(String[] args) {
        Dicionario dicionario = new Dicionario();
        Scanner scanner = new Scanner(System.in);

        // O programa so apresenta o menu depois que um dicionario valido e carregado.
        carregarDicionario(dicionario, scanner);
        boolean running = true;
        // Repeticao principal do menu.
        while (running) {
            printMenu();
            System.out.print("Opcao: ");

            try {
                int option = Integer.parseInt(scanner.nextLine().trim());
                // Cada opcao chama uma funcionalidade do Dicionario.
                switch (option) {
                    case 1:
                        consultarSugestoes(dicionario, scanner);
                        break;
                    case 2:
                        System.out.print("Palavra: ");
                        System.out.println(dicionario.buscar(scanner.nextLine())
                                ? "Palavra encontrada." : "Palavra nao encontrada.");
                        break;
                    case 3:
                        System.out.print("Nova palavra: ");
                        dicionario.adicionar(scanner.nextLine());
                        System.out.println("Palavra adicionada.");
                        break;
                    case 4:
                        System.out.print("Quantidade: ");
                        printRanking(dicionario, Integer.parseInt(scanner.nextLine().trim()));
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                }
            // Trata entradas que deveriam ser numeros, mas receberam outro texto.
            } catch (NumberFormatException exception) {
                System.out.println("Digite uma opcao numerica.");
            // Trata palavras e escolhas invalidas informadas pelo usuario.
            } catch (IllegalArgumentException exception) {
                System.out.println("Entrada invalida: " + exception.getMessage());
            }
        }
        // Libera o leitor de teclado antes de encerrar o programa.
        scanner.close();
    }

    // Pede um caminho de arquivo ate conseguir carregar o dicionario com sucesso.
    private static void carregarDicionario(Dicionario dicionario, Scanner scanner) {
        boolean loaded = false;
        // Repete a pergunta quando o caminho e invalido ou o arquivo nao pode ser lido.
        while (!loaded) {
            System.out.print("Caminho do arquivo do dicionario: ");
            try {
                int words = dicionario.carregar(scanner.nextLine().trim());
                System.out.println(words + " palavras carregadas.");
                loaded = true;
            // Erros de abertura e leitura do arquivo nao encerram o programa.
            } catch (IOException exception) {
                System.out.println("Nao foi possivel ler o arquivo.");
            } catch (IllegalArgumentException exception) {
                System.out.println("Arquivo contem uma palavra invalida.");
            }
        }
    }

    // Exibe as opcoes disponiveis ao usuario.
    private static void printMenu() {
        System.out.println("\n=== AUTOCOMPLETE ===");
        System.out.println("1 - Sugestoes por prefixo");
        System.out.println("2 - Buscar palavra");
        System.out.println("3 - Adicionar palavra");
        System.out.println("4 - Ranking de frequencia");
        System.out.println("0 - Sair");
    }

    // Busca sugestoes para um prefixo e permite registrar o uso de uma escolha.
    private static void consultarSugestoes(Dicionario dicionario, Scanner scanner) {
        System.out.print("Prefixo: ");
        List<String> suggestions = dicionario.sugerir(scanner.nextLine());
        // Evita pedir uma escolha quando nao ha nenhuma sugestao para exibir.
        if (suggestions.isEmpty()) {
            System.out.println("Nenhuma sugestao encontrada.");
            return;
        }

        // Mostra cada sugestao com a posicao que sera usada para seleciona-la.
        for (int i = 0; i < suggestions.size(); i++) {
            String word = suggestions.get(i);
            System.out.println((i + 1) + " - " + word + " (freq: " + dicionario.getFrequencia(word) + ")");
        }
        System.out.print("Escolha uma palavra (Enter para voltar): ");
        String choice = scanner.nextLine().trim();
        // Enter vazio volta ao menu; um numero registra a palavra escolhida.
        if (!choice.isEmpty()) {
            int index = Integer.parseInt(choice) - 1;
            // Confere se o numero digitado pertence a lista mostrada.
            if (index < 0 || index >= suggestions.size()) {
                throw new IllegalArgumentException("Numero fora da lista.");
            }
            String selected = suggestions.get(index);
            dicionario.registrarUso(selected);
            System.out.println("Uso registrado para " + selected + ".");
        }
    }

    // Imprime o ranking global de palavras mais usadas.
    private static void printRanking(Dicionario dicionario, int quantity) {
        List<String> ranking = dicionario.getTopPalavras(quantity);
        // Cada palavra recebe uma posicao e exibe sua frequencia atual.
        for (int i = 0; i < ranking.size(); i++) {
            String word = ranking.get(i);
            System.out.println((i + 1) + " - " + word + " (freq: " + dicionario.getFrequencia(word) + ")");
        }
    }
}

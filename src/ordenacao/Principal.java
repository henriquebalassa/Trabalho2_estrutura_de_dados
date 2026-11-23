package ordenacao;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Teste rápido: roda cada algoritmo nos 3 padrões e nas 2 chaves e mostra tempo,
 * comparações e movimentações. Ainda NÃO é o benchmark completo (sem repetições).
 *
 * Uso:  java -cp out ordenacao.Principal [dados/amostra.csv] [n] [todos|rapidos|seguros]
 */
public class Principal {

    public static void main(String[] args) throws Exception {
        Path arquivo = Path.of(args.length > 0 ? args[0] : "dados/amostra.csv");
        int n = args.length > 1 ? Integer.parseInt(args[1]) : 10_000;

        System.out.println("Carregando " + n + " avaliações de " + arquivo + " ...");
        Avaliacao[] base = Carregador.carregar(arquivo, n);

        // para incluir um novo algoritmo, é só acrescentar nesta lista
        List<Ordenador> todos = List.of(
                new InsertionSort(), new SelectionSort(),
                new QuickSort(QuickSort.Pivo.PRIMEIRO),
                new QuickSort(QuickSort.Pivo.MEDIANA_DE_TRES),
                new QuickSort(QuickSort.Pivo.ALEATORIO),
                new MergeSort());

        // terceiro argumento (opcional):
        //   todos    - roda tudo (padrão)
        //   rapidos  - pula o Insertion e o Selection Sort (quadráticos)
        //   seguros  - pula também o Quick Sort de pivô "primeiro", que é quadrático em entrada
        //              ordenada ou reversa. Use para n grande (ex.: 1.000.000).
        String modo = args.length > 2 ? args[2] : "todos";
        boolean semQuadraticos = modo.equals("rapidos") || modo.equals("seguros");
        boolean semPivoRuim = modo.equals("seguros");
        List<Ordenador> algoritmos = todos.stream()
                .filter(a -> !(semQuadraticos && (a instanceof InsertionSort || a instanceof SelectionSort)))
                .filter(a -> !(semPivoRuim && a instanceof QuickSort q && q.pivo() == QuickSort.Pivo.PRIMEIRO))
                .toList();

        // aquecimento: uma ordenação pequena de cada algoritmo, só para a JVM compilar o código
        Avaliacao[] pequena = Carregador.carregar(arquivo, Math.min(n, 2000));
        for (Ordenador a : algoritmos) {
            for (Chave c : Chave.values()) {
                a.ordenar(Padrao.ALEATORIO.gerar(pequena, c), c);
            }
        }

        for (Ordenador algoritmo : algoritmos) {
            System.out.printf(Locale.ROOT, "%n%s, n = %,d%n", algoritmo.nome(), n);
            System.out.printf(Locale.ROOT, "%-10s %-10s %12s %18s %16s  %s%n",
                    "chave", "padrão", "tempo (ms)", "comparações", "movimentações", "correto?");

            for (Chave chave : Chave.values()) {
                for (Padrao padrao : Padrao.values()) {
                    Avaliacao[] entrada = padrao.gerar(base, chave);   // fora da medição
                    Avaliacao[] trabalho = entrada.clone();

                    long t0 = System.nanoTime();
                    algoritmo.ordenar(trabalho, chave);
                    long t1 = System.nanoTime();

                    boolean ok = Verificacao.correto(trabalho, entrada, chave);
                    System.out.printf(Locale.ROOT, "%-10s %-10s %12.1f %,18d %,16d  %s%n",
                            chave, padrao, (t1 - t0) / 1e6, algoritmo.comparacoes(),
                            algoritmo.movimentacoes(), ok ? "sim" : "NÃO");
                }
            }
        }
    }
}

package ordenacao;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Benchmark completo: todos os algoritmos x 2 chaves x 3 padrões x tamanhos.
 *
 * Uso:  java -cp out ordenacao.Benchmark [opções]
 *
 *   --arquivo=dados/amostra.csv        amostra gerada por preparar_dados.py
 *   --tamanhos=10000,100000,1000000    tamanhos (os N primeiros registros da amostra)
 *   --reps=5                           repetições por experimento
 *   --limite=120                       tempo máximo de UMA execução, em segundos
 *   --limite-rep=20                    se uma execução passar disso, não repete (inviável)
 *   --saida=resultados/tabelas         pasta de saída (resultados.csv e ambiente.txt)
 *

 */
public class Benchmark {

    private record Resultado(String status, int repeticoes, double mediaMs, double desvioMs, double minimoMs,
                             long comparacoes, long movimentacoes) {
    }

    public static void main(String[] args) throws Exception {
        Path arquivo = Path.of(opcao(args, "arquivo", "dados/amostra.csv"));
        long[] tamanhos = Arrays.stream(opcao(args, "tamanhos", "10000,100000,1000000").split(","))
                .mapToLong(Long::parseLong).toArray();
        int reps = Integer.parseInt(opcao(args, "reps", "5"));
        long limiteNs = (long) (Double.parseDouble(opcao(args, "limite", "120")) * 1e9);
        double limiteRepSeg = Double.parseDouble(opcao(args, "limite-rep", "20"));
        Path pasta = Path.of(opcao(args, "saida", "resultados/tabelas"));

        Files.createDirectories(pasta);
        Ambiente.gravar(pasta.resolve("ambiente.txt"));
        System.out.println(Ambiente.descricao());

        int maior = (int) Arrays.stream(tamanhos).max().orElseThrow();
        System.out.println("Carregando " + maior + " avaliações de " + arquivo + " ...");
        Avaliacao[] base = Carregador.carregar(arquivo, maior);

        List<Ordenador> algoritmos = List.of(
                new InsertionSort(), new SelectionSort(),
                new QuickSort(QuickSort.Pivo.PRIMEIRO),
                new QuickSort(QuickSort.Pivo.MEDIANA_DE_TRES),
                new QuickSort(QuickSort.Pivo.ALEATORIO),
                new MergeSort());

        aquecer(algoritmos, base);

        Set<String> interrompidos = new HashSet<>();
        Path csv = pasta.resolve("resultados.csv");
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(csv))) {
            out.println("algoritmo,chave,padrao,n,repeticoes,tempo_medio_ms,desvio_padrao_ms,tempo_min_ms,"
                    + "comparacoes,movimentacoes,status");

            for (long tam : tamanhos) {
                int n = (int) tam;
                Avaliacao[] prefixo = Arrays.copyOf(base, n);   // os n primeiros = amostra aleatória
                for (Chave chave : Chave.values()) {
                    long[] referencia = Verificacao.chavesOrdenadas(prefixo, chave);
                    for (Padrao padrao : Padrao.values()) {
                        Avaliacao[] entrada = padrao.gerar(prefixo, chave);
                        for (Ordenador alg : algoritmos) {
                            String id = alg.nome() + "|" + chave + "|" + padrao;
                            Resultado r;
                            if (interrompidos.contains(id)) {
                                r = new Resultado("NAO_EXECUTADO", 0, 0, 0, 0, 0, 0);
                            } else {
                                r = medir(alg, entrada, chave, referencia, reps, limiteNs, limiteRepSeg);
                                if (r.status().equals("INTERROMPIDO")) {
                                    interrompidos.add(id);
                                }
                            }
                            gravar(out, alg, chave, padrao, n, r);
                            imprimir(alg, chave, padrao, n, r);
                        }
                    }
                }
            }
        }
        System.out.println("\nPronto. Resultados em " + csv);
    }

    private static void aquecer(List<Ordenador> algoritmos, Avaliacao[] base) {
        System.out.println("Aquecendo a JVM ...");
        Avaliacao[] pequena = Arrays.copyOf(base, Math.min(20_000, base.length));
        for (Ordenador alg : algoritmos) {
            for (Chave chave : Chave.values()) {
                for (int i = 0; i < 2; i++) {
                    Avaliacao[] t = Padrao.ALEATORIO.gerar(pequena, chave);
                    alg.ordenar(t, chave);
                }
            }
        }
    }

    private static Resultado medir(Ordenador alg, Avaliacao[] entrada, Chave chave, long[] referencia,
                                   int reps, long limiteNs, double limiteRepSeg) {
        List<Double> tempos = new ArrayList<>();
        long comps = 0;
        long movs = 0;
        for (int r = 0; r < reps; r++) {
            Avaliacao[] trabalho = entrada.clone();   // cópia fora da medição
            System.gc();
            long t0 = System.nanoTime();
            try {
                alg.ordenar(trabalho, chave, limiteNs);
            } catch (Ordenador.TempoEsgotado e) {
                double ms = (System.nanoTime() - t0) / 1e6;
                return new Resultado("INTERROMPIDO", 0, ms, 0, ms, alg.comparacoes(), alg.movimentacoes());
            }
            long dt = System.nanoTime() - t0;

            if (!Verificacao.correto(trabalho, referencia, chave)) {
                throw new IllegalStateException("Resultado INCORRETO: " + alg.nome() + ", " + chave);
            }
            if (r == 0) {
                comps = alg.comparacoes();
                movs = alg.movimentacoes();
            } else if (comps != alg.comparacoes() || movs != alg.movimentacoes()) {
                System.out.println("  AVISO: contagem diferente entre repetições em " + alg.nome());
            }
            tempos.add(dt / 1e6);
            if (dt / 1e9 > limiteRepSeg) {
                break;   // execução longa demais para repetir
            }
        }
        double media = tempos.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double min = tempos.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double desvio = 0;
        if (tempos.size() > 1) {
            double soma = 0;
            for (double t : tempos) {
                soma += (t - media) * (t - media);
            }
            desvio = Math.sqrt(soma / (tempos.size() - 1));
        }
        return new Resultado("OK", tempos.size(), media, desvio, min, comps, movs);
    }

    private static void gravar(PrintWriter out, Ordenador alg, Chave chave, Padrao padrao, int n, Resultado r) {
        out.printf(Locale.ROOT, "%s,%s,%s,%d,%d,%.3f,%.3f,%.3f,%d,%d,%s%n",
                alg.nome(), chave, padrao, n, r.repeticoes(), r.mediaMs(), r.desvioMs(), r.minimoMs(),
                r.comparacoes(), r.movimentacoes(), r.status());
        out.flush();   // se o programa for interrompido, o que já foi medido fica salvo
    }

    private static void imprimir(Ordenador alg, Chave chave, Padrao padrao, int n, Resultado r) {
        String tempo = switch (r.status()) {
            case "OK" -> String.format(Locale.ROOT, "%10.1f ms (±%.1f, %d reps)", r.mediaMs(), r.desvioMs(), r.repeticoes());
            case "INTERROMPIDO" -> String.format(Locale.ROOT, "interrompido após %.1f s", r.mediaMs() / 1000);
            default -> "não executado (interrompido em tamanho menor)";
        };
        System.out.printf(Locale.ROOT, "n=%,9d %-9s %-9s %-36s %s%n", n, chave, padrao, alg.nome(), tempo);
    }

    private static String opcao(String[] args, String nome, String padrao) {
        String prefixo = "--" + nome + "=";
        for (String a : args) {
            if (a.startsWith(prefixo)) {
                return a.substring(prefixo.length());
            }
        }
        return padrao;
    }
}

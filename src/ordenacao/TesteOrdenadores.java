package ordenacao;

import java.util.Random;

/**
 * Teste de correção: roda todos os algoritmos em milhares de vetores pequenos aleatórios
 * (inclusive vazios, de 1 elemento e com MUITAS chaves repetidas) e confere cada resultado.
 *
 * Uso:  java -cp out ordenacao.TesteOrdenadores
 */
public class TesteOrdenadores {

    /** Chaves iguais aparecem no resultado na mesma ordem em que estavam na entrada? */
    private static boolean estavel(Avaliacao[] resultado, Avaliacao[] entrada, Chave chave) {
        java.util.IdentityHashMap<Avaliacao, Integer> posicao = new java.util.IdentityHashMap<>();
        for (int i = 0; i < entrada.length; i++) {
            posicao.put(entrada[i], i);
        }
        for (int i = 1; i < resultado.length; i++) {
            if (chave.valor(resultado[i - 1]) == chave.valor(resultado[i])
                    && posicao.get(resultado[i - 1]) > posicao.get(resultado[i])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Random r = new Random(7);
        Ordenador[] algoritmos = {
            new InsertionSort(), new SelectionSort(),
            new QuickSort(QuickSort.Pivo.PRIMEIRO),
            new QuickSort(QuickSort.Pivo.MEDIANA_DE_TRES),
            new QuickSort(QuickSort.Pivo.ALEATORIO),
            new MergeSort()
        };

        int casos = 0;
        int falhas = 0;
        for (int rodada = 0; rodada < 3000; rodada++) {
            int n = r.nextInt(60);
            int distintos = 1 + r.nextInt(12);   // poucos valores distintos => muitas chaves repetidas
            Avaliacao[] v = new Avaliacao[n];
            for (int i = 0; i < n; i++) {
                v[i] = new Avaliacao(i, i, 0.5 * (1 + r.nextInt(distintos)), r.nextInt(distintos * 3));
            }
            for (Chave chave : Chave.values()) {
                for (Padrao padrao : Padrao.values()) {
                    for (Ordenador alg : algoritmos) {
                        Avaliacao[] entrada = padrao.gerar(v, chave);
                        Avaliacao[] trabalho = entrada.clone();
                        alg.ordenar(trabalho, chave);
                        casos++;
                        if (!Verificacao.correto(trabalho, entrada, chave)) {
                            falhas++;
                            System.out.println("FALHOU: " + alg.nome() + ", n = " + n + ", " + chave + ", " + padrao);
                        }
                        // estabilidade: só Insertion Sort e Merge Sort prometem manter a ordem de chaves iguais
                        if ((alg instanceof InsertionSort || alg instanceof MergeSort)
                                && !estavel(trabalho, entrada, chave)) {
                            falhas++;
                            System.out.println("INSTÁVEL: " + alg.nome() + ", n = " + n + ", " + chave + ", " + padrao);
                        }
                    }
                }
            }
        }
        if (falhas == 0) {
            System.out.println("OK: " + casos + " casos passaram.");
        } else {
            System.out.println(falhas + " falhas em " + casos + " casos.");
            System.exit(1);
        }
    }
}

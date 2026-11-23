package ordenacao;

import java.util.Random;

/**
 * Quick Sort (ordenação rápida).
 *
 * Ideia: escolhe um PIVÔ e reorganiza o vetor de modo que os menores fiquem de um lado
 * e os maiores do outro (particionamento). Depois ordena cada lado do mesmo jeito.
 *
 * Estratégia de pivô (é o que decide o desempenho):
 *  - PRIMEIRO: usa o primeiro elemento. Simples, mas com a entrada já ordenada ou reversa o
 *    pivô é sempre o menor (ou o maior), a divisão fica 1 contra n-1 e o custo vira O(n²).
 *  - MEDIANA_DE_TRES: usa a mediana entre o primeiro, o do meio e o último. Resolve as
 *    entradas ordenada e reversa, que passam a dividir ao meio.
 *  - ALEATORIO: sorteia o pivô (semente fixa). Nenhum padrão de entrada é sempre ruim.
 *
 * Particionamento de Hoare: dois índices andam um em direção ao outro e PARAM em chaves
 * iguais ao pivô. Isso divide bem os vetores com muitas chaves repetidas (a chave NOTA
 * tem só 10 valores). O particionamento de Lomuto, mais simples, degenera em O(n²) quando
 * há muita chave igual.
 *
 * Pilha: a recursão é feita só na MENOR das duas partes e a maior vira um laço. Assim a
 * profundidade da recursão fica abaixo de log2(n), mesmo no pior caso, e não estoura a pilha.
 *
 * Complexidade: melhor e médio caso O(n log n); pior caso O(n²) (pivô ruim).
 * Não é estável.
 */
public class QuickSort extends Ordenador {

    public enum Pivo {
        PRIMEIRO("primeiro elemento"),
        MEDIANA_DE_TRES("mediana de três"),
        ALEATORIO("aleatório");

        private final String descricao;

        Pivo(String descricao) {
            this.descricao = descricao;
        }
    }

    private static final long SEMENTE = 2026;

    private final Pivo estrategia;
    private Random sorteio;

    public QuickSort(Pivo estrategia) {
        this.estrategia = estrategia;
    }

    public Pivo pivo() {
        return estrategia;
    }

    @Override
    public String nome() {
        return "Quick Sort (pivô: " + estrategia.descricao + ")";
    }

    @Override
    protected void executar(Avaliacao[] v) {
        sorteio = new Random(SEMENTE);   // mesma sequência de sorteios em toda execução
        ordenar(v, 0, v.length - 1);
    }

    private void ordenar(Avaliacao[] v, int ini, int fim) {
        while (ini < fim) {
            int j = particionar(v, ini, fim);   // [ini..j] <= [j+1..fim]
            // recursão só na parte menor; a maior continua no laço
            if (j - ini < fim - j) {
                ordenar(v, ini, j);
                ini = j + 1;
            } else {
                ordenar(v, j + 1, fim);
                fim = j;
            }
        }
    }

    /** Particionamento de Hoare. O pivô é levado para a posição ini antes de começar. */
    private int particionar(Avaliacao[] v, int ini, int fim) {
        int escolhido = escolherPivo(v, ini, fim);
        if (escolhido != ini) {
            trocar(v, ini, escolhido);
        }
        Avaliacao pivo = v[ini];

        int i = ini - 1;
        int j = fim + 1;
        while (true) {
            do {
                i++;
            } while (comparar(v[i], pivo) < 0);
            do {
                j--;
            } while (comparar(v[j], pivo) > 0);
            if (i >= j) {
                return j;
            }
            trocar(v, i, j);
        }
    }

    private int escolherPivo(Avaliacao[] v, int ini, int fim) {
        switch (estrategia) {
            case PRIMEIRO:
                return ini;
            case ALEATORIO:
                return ini + sorteio.nextInt(fim - ini + 1);
            default:
                return medianaDeTres(v, ini, ini + (fim - ini) / 2, fim);
        }
    }

    /** Devolve o índice (entre a, b e c) cujo elemento tem a chave do meio. */
    private int medianaDeTres(Avaliacao[] v, int a, int b, int c) {
        if (comparar(v[a], v[b]) < 0) {
            if (comparar(v[b], v[c]) < 0) {
                return b;                                  // a < b < c
            }
            return comparar(v[a], v[c]) < 0 ? c : a;       // c <= b: o meio é c ou a
        }
        if (comparar(v[a], v[c]) < 0) {
            return a;                                      // b <= a < c
        }
        return comparar(v[b], v[c]) < 0 ? c : b;           // c <= a: o meio é c ou b
    }
}

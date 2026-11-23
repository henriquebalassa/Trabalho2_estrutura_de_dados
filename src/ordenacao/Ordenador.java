package ordenacao;

/**
 * Base de todos os algoritmos de ordenação. Guarda as métricas:
 *
 *  - comparações: TODA comparação entre duas chaves passa pelo método comparar();
 *  - movimentações: toda escrita em uma posição do vetor (uma troca conta como 2).
 *
 * Cada algoritmo só implementa executar().
 */
public abstract class Ordenador {

    /** Lançada quando uma execução passa do tempo máximo permitido (ver ordenar com limite). */
    public static final class TempoEsgotado extends RuntimeException {
        TempoEsgotado() {
            super("tempo máximo por execução excedido", null, false, false);
        }
    }

    // a cada 2^20 comparações (cerca de 1 milhão) confere o relógio; custo desprezível
    private static final long MASCARA_RELOGIO = (1L << 20) - 1;

    protected long comparacoes;
    protected long movimentacoes;
    private Chave chave;
    private long inicioNs;
    private long limiteNs = Long.MAX_VALUE;

    public abstract String nome();

    protected abstract void executar(Avaliacao[] v);

    /** Ordena v (em ordem crescente da chave) e zera as métricas antes de começar. */
    public final void ordenar(Avaliacao[] v, Chave chave) {
        ordenar(v, chave, Long.MAX_VALUE);
    }

    /**
     * Igual ao anterior, mas se passar de limiteNs nanossegundos lança TempoEsgotado.
     * Quando isso acontece, comparacoes() e movimentacoes() trazem o que foi feito até ali
     * e o vetor fica parcialmente ordenado (deve ser descartado).
     */
    public final void ordenar(Avaliacao[] v, Chave chave, long limiteNs) {
        this.chave = chave;
        this.comparacoes = 0;
        this.movimentacoes = 0;
        this.limiteNs = limiteNs;
        this.inicioNs = System.nanoTime();
        executar(v);
    }

    public final long comparacoes() {
        return comparacoes;
    }

    public final long movimentacoes() {
        return movimentacoes;
    }

    /** Única porta de comparação: negativo se a < b, zero se iguais, positivo se a > b. */
    protected final int comparar(Avaliacao a, Avaliacao b) {
        if ((++comparacoes & MASCARA_RELOGIO) == 0 && System.nanoTime() - inicioNs > limiteNs) {
            throw new TempoEsgotado();
        }
        return Long.compare(chave.valor(a), chave.valor(b));
    }

    protected final void trocar(Avaliacao[] v, int i, int j) {
        Avaliacao t = v[i];
        v[i] = v[j];
        v[j] = t;
        movimentacoes += 2;
    }
}

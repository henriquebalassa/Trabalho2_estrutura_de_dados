package ordenacao;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Padrão da entrada: como os dados chegam ao algoritmo.
 *
 * A amostra do arquivo já está em ordem aleatória (semente fixa). Os outros dois padrões
 * são gerados a partir dela. ATENÇÃO: o Arrays.sort do Java é usado aqui APENAS para
 * preparar a entrada, fora da medição. Ele nunca é um dos algoritmos avaliados.
 */
public enum Padrao {
    ORDENADO, REVERSO, ALEATORIO;

    /** Devolve uma CÓPIA de base, no padrão pedido. A base não é alterada. */
    public Avaliacao[] gerar(Avaliacao[] base, Chave chave) {
        Avaliacao[] v = base.clone();
        Comparator<Avaliacao> cmp = Comparator.comparingLong(chave::valor);
        switch (this) {
            case ORDENADO -> Arrays.sort(v, cmp);
            case REVERSO -> Arrays.sort(v, cmp.reversed());
            case ALEATORIO -> { /* já está embaralhada */ }
        }
        return v;
    }
}

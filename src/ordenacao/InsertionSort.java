package ordenacao;

/**
 * Insertion Sort (ordenação por inserção).
 *
 * Ideia: o começo do vetor já está ordenado. Pega o próximo elemento, "empurra" para a
 * direita os maiores que ele e coloca ele no buraco que sobrou.
 *
 * Complexidade: melhor caso O(n) (já ordenado: 1 comparação por elemento),
 *               caso médio e pior caso O(n²) (reverso: cada elemento anda até o começo).
 * É estável: só empurra quando o da esquerda é ESTRITAMENTE maior.
 */
public class InsertionSort extends Ordenador {

    @Override
    public String nome() {
        return "Insertion Sort";
    }

    @Override
    protected void executar(Avaliacao[] v) {
        for (int i = 1; i < v.length; i++) {
            Avaliacao x = v[i];
            int j = i - 1;
            while (j >= 0 && comparar(v[j], x) > 0) {
                v[j + 1] = v[j];
                movimentacoes++;
                j--;
            }
            if (j + 1 != i) {      // só escreve se o elemento mudou de lugar
                v[j + 1] = x;
                movimentacoes++;
            }
        }
    }
}

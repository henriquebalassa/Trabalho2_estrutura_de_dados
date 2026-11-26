package ordenacao;

/**
 * Merge Sort (ordenação por intercalação).
 *
 * Ideia (dividir para conquistar): divide o vetor ao meio, ordena cada metade do mesmo
 * jeito e depois INTERCALA as duas metades já ordenadas em um vetor só.
 *
 
 */
public class MergeSort extends Ordenador {

    private Avaliacao[] aux;

    @Override
    public String nome() {
        return "Merge Sort";
    }

    @Override
    protected void executar(Avaliacao[] v) {
        aux = new Avaliacao[v.length / 2 + 1];
        ordenar(v, 0, v.length - 1);
        aux = null;
    }

    private void ordenar(Avaliacao[] v, int ini, int fim) {
        if (ini >= fim) {
            return;
        }
        int meio = ini + (fim - ini) / 2;
        ordenar(v, ini, meio);
        ordenar(v, meio + 1, fim);
        intercalar(v, ini, meio, fim);
    }

    /**
     * Intercala v[ini..meio] e v[meio+1..fim], que já estão ordenados cada um.
     * Copia só a metade esquerda para o vetor auxiliar e vai escrevendo o resultado em v.
     * O índice de escrita nunca ultrapassa o de leitura da metade direita, então nada é
     * sobrescrito antes de ser lido.
     */
    private void intercalar(Avaliacao[] v, int ini, int meio, int fim) {
        int nEsq = meio - ini + 1;
        for (int i = 0; i < nEsq; i++) {
            aux[i] = v[ini + i];
        }
        movimentacoes += nEsq;

        int i = 0;            // posição na metade esquerda (em aux)
        int j = meio + 1;     // posição na metade direita (em v)
        int k = ini;          // posição de escrita em v
        while (i < nEsq && j <= fim) {
            // só pega da direita se for ESTRITAMENTE menor: em empate vale a esquerda (estável)
            if (comparar(v[j], aux[i]) < 0) {
                v[k++] = v[j++];
            } else {
                v[k++] = aux[i++];
            }
            movimentacoes++;
        }
        while (i < nEsq) {    // sobrou algo da esquerda; o que sobrar da direita já está no lugar
            v[k++] = aux[i++];
            movimentacoes++;
        }
    }
}

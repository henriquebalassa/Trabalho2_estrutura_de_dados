package ordenacao;

/**
 * Selection Sort (ordenação por seleção).
 *
 * Ideia: a cada passo, procura o MENOR elemento da parte ainda não ordenada e troca ele
 * com o primeiro dessa parte. A parte ordenada cresce uma posição por vez.
 *
 * Complexidade: sempre O(n²) em comparações, em qualquer padrão de entrada, porque ele
 * sempre varre todo o resto do vetor (exatamente n(n-1)/2 comparações). Em compensação faz
 * poucas movimentações: no máximo n-1 trocas.
 * Não é estável: a troca pode passar um elemento por cima de outro de chave igual.
 */
public class SelectionSort extends Ordenador {

    @Override
    public String nome() {
        return "Selection Sort";
    }

    @Override
    protected void executar(Avaliacao[] v) {
        int n = v.length;
        for (int i = 0; i < n - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < n; j++) {
                if (comparar(v[j], v[menor]) < 0) {
                    menor = j;
                }
            }
            if (menor != i) {      // só troca se o menor não já estiver no lugar
                trocar(v, i, menor);
            }
        }
    }
}

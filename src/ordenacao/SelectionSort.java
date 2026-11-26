package ordenacao;

/**
 * Selection Sort (ordenação por seleção).
 *
 * Ideia: a cada passo, procura o MENOR elemento da parte ainda não ordenada e troca ele
 * com o primeiro dessa parte. A parte ordenada cresce uma posição por vez.
 *
 
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

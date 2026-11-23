package ordenacao;

import java.util.Arrays;
import java.util.Comparator;

/** Confere se o resultado de um algoritmo está correto. */
public final class Verificacao {

    private Verificacao() {
    }

    /** Cada chave é maior ou igual à anterior? */
    public static boolean ordenado(Avaliacao[] v, Chave chave) {
        for (int i = 1; i < v.length; i++) {
            if (chave.valor(v[i - 1]) > chave.valor(v[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Versão rápida: compara com as chaves de referência já ordenadas (calculadas uma vez
     * por tamanho e chave), em vez de reordenar a entrada a cada execução.
     */
    public static boolean correto(Avaliacao[] resultado, long[] chavesReferencia, Chave chave) {
        if (resultado.length != chavesReferencia.length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            if (chave.valor(resultado[i]) != chavesReferencia[i]) {
                return false;
            }
        }
        return true;   // igual à referência ordenada => está ordenado e não perdeu elementos
    }

    /** Chaves da entrada em ordem crescente (referência de correção; Arrays.sort só aqui). */
    public static long[] chavesOrdenadas(Avaliacao[] entrada, Chave chave) {
        long[] k = new long[entrada.length];
        for (int i = 0; i < k.length; i++) {
            k[i] = chave.valor(entrada[i]);
        }
        Arrays.sort(k);
        return k;
    }

    /**
     * Está ordenado E tem exatamente as mesmas chaves da entrada original?
     * (Garante que o algoritmo não perdeu nem duplicou elementos.) O Arrays.sort é usado só
     * como REFERÊNCIA de correção, como permite o enunciado.
     */
    public static boolean correto(Avaliacao[] resultado, Avaliacao[] entradaOriginal, Chave chave) {
        if (!ordenado(resultado, chave)) {
            return false;
        }
        Avaliacao[] ref = entradaOriginal.clone();
        Arrays.sort(ref, Comparator.comparingLong(chave::valor));
        for (int i = 0; i < ref.length; i++) {
            if (chave.valor(ref[i]) != chave.valor(resultado[i])) {
                return false;
            }
        }
        return true;
    }
}

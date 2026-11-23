package ordenacao;

/**
 * Campo usado como chave de ordenação. Os dois viram um número inteiro (long),
 * então comparar duas avaliações é sempre comparar dois longs.
 *
 *  - TIMESTAMP: instante da avaliação (segundos). Quase não tem valores repetidos.
 *  - NOTA: a nota de 0,5 a 5,0 multiplicada por 2 (vira 1 a 10, já calculado na
 *          Avaliacao). Só 10 valores distintos, então há muitíssimas chaves repetidas.
 */
public enum Chave {
    TIMESTAMP {
        @Override
        long valor(Avaliacao a) {
            return a.timestamp();
        }
    },
    NOTA {
        @Override
        long valor(Avaliacao a) {
            return a.nota2();
        }
    };

    abstract long valor(Avaliacao a);
}

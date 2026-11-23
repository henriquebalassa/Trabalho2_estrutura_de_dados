package ordenacao;

/**
 * Uma avaliação do MovieLens: um usuário deu uma nota a um filme em um instante.
 *
 * A nota (0,5 a 5,0) também é guardada como inteiro (nota2 = nota x 2, de 1 a 10), calculado
 * UMA vez na criação. Assim, comparar duas avaliações pela nota custa o mesmo que compará-las
 * pelo timestamp, e o tempo medido não é distorcido por uma conta feita a cada comparação.
 */
public final class Avaliacao {

    private final int userId;
    private final int movieId;
    private final double rating;
    private final long timestamp;
    private final int nota2;

    public Avaliacao(int userId, int movieId, double rating, long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
        this.nota2 = (int) Math.round(rating * 2);
    }

    public int userId() {
        return userId;
    }

    public int movieId() {
        return movieId;
    }

    public double rating() {
        return rating;
    }

    public long timestamp() {
        return timestamp;
    }

    int nota2() {
        return nota2;
    }
}

package ordenacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Lê as n primeiras avaliações de dados/amostra.csv (userId,movieId,rating,timestamp). */
public final class Carregador {

    private Carregador() {
    }

    public static Avaliacao[] carregar(Path arquivo, int n) throws IOException {
        Avaliacao[] r = new Avaliacao[n];
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            br.readLine(); // cabeçalho
            for (int i = 0; i < n; i++) {
                String linha = br.readLine();
                if (linha == null) {
                    throw new IOException("O arquivo tem só " + i + " registros, mas foram pedidos " + n);
                }
                String[] p = linha.split(",");
                r[i] = new Avaliacao(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                        Double.parseDouble(p[2]), Long.parseLong(p[3]));
            }
        }
        return r;
    }
}

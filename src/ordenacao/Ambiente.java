package ordenacao;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/** Descreve a máquina e o Java usados nos testes (exigido no artigo). */
public final class Ambiente {

    private Ambiente() {
    }

    public static String descricao() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data: ").append(LocalDateTime.now().withNano(0)).append('\n');
        sb.append("Processador: ").append(processador()).append('\n');
        sb.append("Processadores lógicos: ").append(Runtime.getRuntime().availableProcessors()).append('\n');
        sb.append("Memória física: ").append(memoriaFisica()).append('\n');
        sb.append("Memória máxima da JVM: ").append(Runtime.getRuntime().maxMemory() / (1024 * 1024)).append(" MB\n");
        sb.append("Sistema operacional: ").append(System.getProperty("os.name")).append(' ')
                .append(System.getProperty("os.version")).append(" (").append(System.getProperty("os.arch")).append(")\n");
        sb.append("Java: ").append(System.getProperty("java.version")).append(" - ")
                .append(System.getProperty("java.vm.name")).append(" (").append(System.getProperty("java.vendor")).append(")\n");
        return sb.toString();
    }

    public static void gravar(Path arquivo) throws IOException {
        if (arquivo.getParent() != null) {
            Files.createDirectories(arquivo.getParent());
        }
        Files.writeString(arquivo, descricao());
    }

    private static String processador() {
        String win = System.getenv("PROCESSOR_IDENTIFIER");   // existe no Windows
        if (win != null && !win.isBlank()) {
            return win;
        }
        try {
            Path cpu = Path.of("/proc/cpuinfo");               // existe no Linux
            if (Files.exists(cpu)) {
                List<String> linhas = Files.readAllLines(cpu);
                for (String l : linhas) {
                    if (l.startsWith("model name")) {
                        return l.substring(l.indexOf(':') + 1).trim();
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            // cai no valor padrão abaixo
        }
        return "não identificado (" + System.getProperty("os.arch") + ")";
    }

    private static String memoriaFisica() {
        try {
            var so = ManagementFactory.getOperatingSystemMXBean();
            if (so instanceof com.sun.management.OperatingSystemMXBean ext) {
                return ext.getTotalMemorySize() / (1024 * 1024) + " MB";
            }
        } catch (RuntimeException | LinkageError e) {
            // cai no valor padrão abaixo
        }
        return "não identificada";
    }
}

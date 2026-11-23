"""
Gera os gráficos e as tabelas do artigo a partir de resultados/tabelas/resultados.csv.

Uso (na pasta raiz do projeto):
    python scripts/gerar_graficos.py
    python scripts/gerar_graficos.py --limite 120        (limite de tempo usado no benchmark, em s)

Saídas:
    resultados/graficos/tempo_timestamp.png
    resultados/graficos/tempo_nota.png
    resultados/graficos/comparacoes_timestamp.png
    resultados/graficos/quicksort_pivo.png
    resultados/tabelas/tabelas.md      (todas as tabelas, para ler e para o README)
    resultados/tabelas/tabelas.tex     (as mesmas tabelas em LaTeX, para o artigo)

Dependência: matplotlib  (pip install matplotlib)
"""
import argparse
import csv
import math
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D
from matplotlib.ticker import FuncFormatter, NullFormatter

# (nome no CSV, nome curto, cor, marcador). Cores da paleta Okabe-Ito (legíveis por daltônicos).
ALGS = [
    ("Insertion Sort", "Insertion Sort", "#0072B2", "o"),
    ("Selection Sort", "Selection Sort", "#E69F00", "s"),
    ("Quick Sort (pivô: primeiro elemento)", "Quick Sort, pivô primeiro elemento", "#D55E00", "^"),
    ("Quick Sort (pivô: mediana de três)", "Quick Sort, pivô mediana de três", "#009E73", "v"),
    ("Quick Sort (pivô: aleatório)", "Quick Sort, pivô aleatório", "#CC79A7", "D"),
    ("Merge Sort", "Merge Sort", "#333333", "P"),
]
PADROES = [("ORDENADO", "Entrada já ordenada"), ("REVERSO", "Entrada em ordem reversa"),
           ("ALEATORIO", "Entrada aleatória")]
CHAVES = [("TIMESTAMP", "timestamp"), ("NOTA", "nota")]
TAMANHOS = [10_000, 100_000, 1_000_000]
NOME_TAM = {10_000: "10 mil", 100_000: "100 mil", 1_000_000: "1 milhão"}


# ---------------------------------------------------------------- leitura

def ler(caminho, limite_ms):
    dados = {}
    with open(caminho, newline="", encoding="utf-8") as f:
        for r in csv.DictReader(f):
            reg = {
                "status": r["status"],
                "reps": int(r["repeticoes"]),
                "media": float(r["tempo_medio_ms"]),
                "dp": float(r["desvio_padrao_ms"]),
                "comp": int(r["comparacoes"]),
                "mov": int(r["movimentacoes"]),
            }
            if reg["status"] == "INTERROMPIDO" and reg["media"] > 1.5 * limite_ms:
                print(f"AVISO: {r['algoritmo']} / {r['chave']} / {r['padrao']} / n={r['n']} foi interrompido só "
                      f"após {reg['media'] / 1000:.0f} s (limite: {limite_ms / 1000:.0f} s). O computador pode ter "
                      f"suspendido nesse trecho. Os gráficos e as tabelas usam o limite, não esse tempo.")
            dados[(r["algoritmo"], r["chave"], r["padrao"], int(r["n"]))] = reg
    return dados


# ---------------------------------------------------------------- formatação (pt-BR)

def milhar(v):
    return f"{v:,.0f}".replace(",", ".")


def num(v, casas=None):
    """Número com vírgula decimal e ponto de milhar."""
    if casas is None:
        casas = 2 if v < 10 else (1 if v < 1000 else 0)
    s = f"{v:,.{casas}f}"
    return s.replace(",", "X").replace(".", ",").replace("X", ".")


def contagem_curta(v):
    if v >= 1e9:
        return f"{v / 1e9:.1f}".replace(".", ",") + " bi"
    if v >= 1e6:
        return f"{v / 1e6:.1f}".replace(".", ",") + " mi"
    if v >= 1e3:
        return f"{v / 1e3:.0f} mil"
    return str(int(v))


def formatar_eixo_log(v, _):
    if v >= 1:
        return milhar(v)
    return str(v).replace(".", ",")


def configurar_x(ax):
    ax.set_xscale("log")
    ax.set_xticks(TAMANHOS)
    ax.set_xticklabels([NOME_TAM[n] for n in TAMANHOS])
    ax.xaxis.set_minor_formatter(NullFormatter())
    ax.set_xlim(7000, 1_450_000)
    ax.grid(True, which="major", color="#DDDDDD", linewidth=0.7)
    ax.set_axisbelow(True)


def legenda_algs(fig, extra=None, ncol=3):
    h = [Line2D([0], [0], color=c, marker=m, lw=1.6, ms=5, label=nome) for _, nome, c, m in ALGS]
    if extra:
        h += extra
    fig.legend(handles=h, loc="lower center", ncol=ncol, frameon=False, fontsize=8.5)


# ---------------------------------------------------------------- gráficos

def grafico_tempo(dados, chave, titulo_chave, saida, limite_ms):
    fig, axes = plt.subplots(1, 3, figsize=(11, 4.6), sharey=True)
    for ax, (padrao, titulo) in zip(axes, PADROES):
        for nome_csv, _, cor, marc in ALGS:
            xs, ys, dps, interrompido = [], [], [], None
            for n in TAMANHOS:
                r = dados.get((nome_csv, chave, padrao, n))
                if r is None or r["status"] == "NAO_EXECUTADO":
                    continue
                if r["status"] == "OK":
                    xs.append(n)
                    ys.append(r["media"])
                    dps.append(r["dp"])
                else:
                    interrompido = n
            ax.errorbar(xs, ys, yerr=dps, color=cor, marker=marc, lw=1.6, ms=5, capsize=2, elinewidth=0.8)
            if interrompido is not None:
                if xs:
                    ax.plot([xs[-1], interrompido], [ys[-1], limite_ms], "--", color=cor, lw=1)
                ax.plot([interrompido], [limite_ms], marker="x", color=cor, ms=9, mew=2.2, zorder=5)
        ax.axhline(limite_ms, color="#888888", linestyle=":", lw=1.2)
        ax.set_yscale("log")
        ax.set_ylim(0.1, limite_ms * 3)
        configurar_x(ax)
        ax.set_title(titulo, fontsize=10)
        ax.set_xlabel("Número de registros")
    axes[0].set_ylabel("Tempo de ordenação (ms, escala logarítmica)")
    axes[0].yaxis.set_major_formatter(FuncFormatter(formatar_eixo_log))
    axes[0].text(8000, limite_ms * 1.25, f"limite: {limite_ms / 1000:.0f} s por execução", fontsize=8, color="#555555")
    fig.suptitle(f"Tempo de ordenação × tamanho do conjunto (chave: {titulo_chave})", fontsize=11)
    extra = [Line2D([0], [0], color="#555555", marker="x", ls="--", lw=1, ms=8, mew=2,
                    label="execução interrompida no limite (tempo > limite)")]
    legenda_algs(fig, extra, ncol=3)
    fig.tight_layout(rect=(0, 0.13, 1, 0.97))
    fig.savefig(saida, dpi=200)
    plt.close(fig)


def grafico_comparacoes(dados, chave, titulo_chave, saida):
    fig, axes = plt.subplots(1, 3, figsize=(11, 4.6), sharey=True)
    grade = [10 ** (4 + i / 20) for i in range(41)]
    for ax, (padrao, titulo) in zip(axes, PADROES):
        ax.plot(grade, [n * n / 2 for n in grade], "--", color="#AAAAAA", lw=1.2)
        ax.plot(grade, [n * math.log2(n) for n in grade], ":", color="#AAAAAA", lw=1.6)
        for nome_csv, _, cor, marc in ALGS:
            xs, ys = [], []
            for n in TAMANHOS:
                r = dados.get((nome_csv, chave, padrao, n))
                if r is not None and r["status"] == "OK":
                    xs.append(n)
                    ys.append(r["comp"])
            ax.plot(xs, ys, color=cor, marker=marc, lw=1.6, ms=5)
        ax.set_yscale("log")
        ax.set_ylim(5e3, 3e12)
        configurar_x(ax)
        ax.set_title(titulo, fontsize=10)
        ax.set_xlabel("Número de registros")
    axes[0].set_ylabel("Comparações entre chaves (escala logarítmica)")
    axes[0].yaxis.set_major_formatter(FuncFormatter(lambda v, _: "$10^{%d}$" % round(math.log10(v))))
    fig.suptitle(f"Comparações × tamanho do conjunto (chave: {titulo_chave}). "
                 "Execuções interrompidas não aparecem.", fontsize=11)
    extra = [Line2D([0], [0], color="#AAAAAA", ls="--", lw=1.2, label=r"referência $n^2/2$"),
             Line2D([0], [0], color="#AAAAAA", ls=":", lw=1.6, label=r"referência $n \log_2 n$")]
    legenda_algs(fig, extra, ncol=4)
    fig.tight_layout(rect=(0, 0.13, 1, 0.97))
    fig.savefig(saida, dpi=200)
    plt.close(fig)


def grafico_pivo(dados, saida, n=100_000):
    fig, axes = plt.subplots(1, 2, figsize=(11, 4.4), sharey=True)
    quicks = [a for a in ALGS if a[0].startswith("Quick")]
    largura = 0.26
    for ax, (chave, titulo_chave) in zip(axes, CHAVES):
        for i, (nome_csv, nome, cor, _) in enumerate(quicks):
            for g, (padrao, _) in enumerate(PADROES):
                r = dados[(nome_csv, chave, padrao, n)]
                x = g + (i - 1) * largura
                ax.bar(x, r["comp"], largura, color=cor, label=nome if g == 0 else None)
                ax.text(x, r["comp"] * 1.25, contagem_curta(r["comp"]), ha="center", fontsize=7.5)
        ax.set_yscale("log")
        ax.set_ylim(1e5, 4e10)
        ax.set_xticks(range(3))
        ax.set_xticklabels(["ordenada", "reversa", "aleatória"])
        ax.set_xlabel("Padrão da entrada")
        ax.set_title(f"Chave: {titulo_chave}", fontsize=10)
        ax.grid(True, axis="y", which="major", color="#DDDDDD", linewidth=0.7)
        ax.set_axisbelow(True)
    axes[0].set_ylabel("Comparações entre chaves (escala logarítmica)")
    axes[0].yaxis.set_major_formatter(FuncFormatter(lambda v, _: "$10^{%d}$" % round(math.log10(v))))
    fig.suptitle(f"Quick Sort: efeito da estratégia de pivô (n = {milhar(n)} registros)", fontsize=11)
    h, l = axes[0].get_legend_handles_labels()
    fig.legend(h, l, loc="lower center", ncol=3, frameon=False, fontsize=9)
    fig.tight_layout(rect=(0, 0.08, 1, 0.96))
    fig.savefig(saida, dpi=200)
    plt.close(fig)


# ---------------------------------------------------------------- tabelas

ACENTOS = {"á": r"\'a", "é": r"\'e", "í": r"\'i", "ó": r"\'o", "ú": r"\'u", "â": r"\^a", "ê": r"\^e",
           "ô": r"\^o", "ã": r"\~a", "õ": r"\~o", "ç": r"\c{c}", "à": r"\`a", "Á": r"\'A", "É": r"\'E",
           "Ó": r"\'O", "Ç": r"\c{C}"}


def latex(txt):
    for a, b in ACENTOS.items():
        txt = txt.replace(a, b)
    return (txt.replace("±", r"$\pm$").replace("> ", r"$>$ ").replace("—", "--").replace("×", r"$\times$")
            .replace("º", r"\textsuperscript{o}"))


def celula_tempo(r, limite_ms):
    if r is None or r["status"] == "NAO_EXECUTADO":
        return "—"
    if r["status"] == "INTERROMPIDO":
        return f"> {limite_ms / 1000:.0f} s"
    if r["reps"] == 1:
        return num(r["media"]) + "*"
    return f"{num(r['media'])} ± {num(r['dp'])}"


def celula_comp(r):
    if r is None or r["status"] != "OK":
        return "—"
    return milhar(r["comp"])


NOMES_TABELA = {
    "Insertion Sort": "Insertion Sort",
    "Selection Sort": "Selection Sort",
    "Quick Sort (pivô: primeiro elemento)": "Quick Sort (pivô: primeiro)",
    "Quick Sort (pivô: mediana de três)": "Quick Sort (pivô: mediana de três)",
    "Quick Sort (pivô: aleatório)": "Quick Sort (pivô: aleatório)",
    "Merge Sort": "Merge Sort",
}


def montar_tabelas(dados, limite_ms):
    """Devolve uma lista de dicts: titulo, rotulo, cabecalho, linhas, nota."""
    tabelas = []
    nota_t = ("Média ± desvio-padrão de 5 repetições. * = execução única (acima de 20 s não se repete). "
              f"> {limite_ms / 1000:.0f} s = execução interrompida no limite de tempo. ")
    nota_c = "— = execução interrompida (contagem incompleta, não comparável)."
    for chave, tchave in CHAVES:
        for n in TAMANHOS:
            linhas = [[NOMES_TABELA[a]] + [celula_tempo(dados.get((a, chave, p, n)), limite_ms)
                                          for p, _ in PADROES] for a, *_ in ALGS]
            tabelas.append({"titulo": f"Tempo de ordenação (ms), chave {tchave}, n = {milhar(n)}",
                            "rotulo": f"tempo-{tchave}-{n}", "linhas": linhas, "nota": nota_t})
    for chave, tchave in CHAVES:
        for n in TAMANHOS:
            linhas = [[NOMES_TABELA[a]] + [celula_comp(dados.get((a, chave, p, n)))
                                          for p, _ in PADROES] for a, *_ in ALGS]
            tabelas.append({"titulo": f"Comparações entre chaves, chave {tchave}, n = {milhar(n)}",
                            "rotulo": f"comp-{tchave}-{n}", "linhas": linhas, "nota": nota_c})
    return tabelas


def gravar_markdown(tabelas, caminho):
    cab = ["Algoritmo"] + [t for _, t in [("O", "Ordenado"), ("R", "Reverso"), ("A", "Aleatório")]]
    partes = ["# Tabelas de resultados\n", "Geradas por `scripts/gerar_graficos.py` a partir de "
              "`resultados/tabelas/resultados.csv`.\n"]
    for t in tabelas:
        partes.append(f"\n### {t['titulo']}\n")
        partes.append("| " + " | ".join(cab) + " |")
        partes.append("|" + "|".join(["---"] + ["---:"] * 3) + "|")
        for linha in t["linhas"]:
            partes.append("| " + " | ".join(linha) + " |")
        partes.append(f"\n_{t['nota']}_")
    Path(caminho).write_text("\n".join(partes) + "\n", encoding="utf-8")


def gravar_latex(tabelas, caminho):
    partes = ["% Tabelas geradas por scripts/gerar_graficos.py. Copie as que for usar no artigo.\n"]
    for t in tabelas:
        partes.append(r"\begin{table}[ht]")
        partes.append(r"\centering")
        partes.append(r"\caption{" + latex(t["titulo"] + ". " + t["nota"]) + "}")
        partes.append(r"\label{tab:" + t["rotulo"] + "}")
        partes.append(r"\begin{tabular}{lrrr}")
        partes.append(r"\hline")
        partes.append(latex("Algoritmo & Ordenado & Reverso & Aleatório") + r" \\")
        partes.append(r"\hline")
        for linha in t["linhas"]:
            partes.append(" & ".join(latex(c) for c in linha) + r" \\")
        partes.append(r"\hline")
        partes.append(r"\end{tabular}")
        partes.append(r"\end{table}")
        partes.append("")
    Path(caminho).write_text("\n".join(partes), encoding="utf-8")


# ---------------------------------------------------------------- principal

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--csv", default="resultados/tabelas/resultados.csv")
    ap.add_argument("--graficos", default="resultados/graficos")
    ap.add_argument("--tabelas", default="resultados/tabelas")
    ap.add_argument("--limite", type=float, default=120, help="limite por execução usado no benchmark (s)")
    a = ap.parse_args()

    limite_ms = a.limite * 1000
    dados = ler(a.csv, limite_ms)
    Path(a.graficos).mkdir(parents=True, exist_ok=True)
    Path(a.tabelas).mkdir(parents=True, exist_ok=True)

    grafico_tempo(dados, "TIMESTAMP", "timestamp", Path(a.graficos) / "tempo_timestamp.png", limite_ms)
    grafico_tempo(dados, "NOTA", "nota", Path(a.graficos) / "tempo_nota.png", limite_ms)
    grafico_comparacoes(dados, "TIMESTAMP", "timestamp", Path(a.graficos) / "comparacoes_timestamp.png")
    grafico_pivo(dados, Path(a.graficos) / "quicksort_pivo.png")

    tabelas = montar_tabelas(dados, limite_ms)
    gravar_markdown(tabelas, Path(a.tabelas) / "tabelas.md")
    gravar_latex(tabelas, Path(a.tabelas) / "tabelas.tex")
    print("Gráficos em", a.graficos, "e tabelas em", a.tabelas)


if __name__ == "__main__":
    main()

"""
Baixa o MovieLens 25M (GroupLens) e sorteia uma amostra de avaliações reais.

Saída: dados/amostra.csv, com as colunas userId,movieId,rating,timestamp, em ORDEM ALEATÓRIA.
Como a amostra já está embaralhada (semente fixa), os N primeiros registros são, eles mesmos,
uma amostra aleatória. Por isso os tamanhos 10 mil e 100 mil saem como os primeiros
10.000 e 100.000 registros do mesmo arquivo (subconjuntos aninhados, sem replicação).

Uso:
    python dados/preparar_dados.py
    python dados/preparar_dados.py --zip caminho/para/ml-25m.zip     (se você já baixou)

Dependência: pandas  (pip install pandas)
"""
import argparse
import sys
import urllib.request
import zipfile
from pathlib import Path

import pandas as pd

URL = "https://files.grouplens.org/datasets/movielens/ml-25m.zip"
MEMBRO = "ml-25m/ratings.csv"


def baixar(destino: Path) -> None:
    print(f"Baixando {URL} (cerca de 250 MB)...")

    def progresso(blocos, tam_bloco, total):
        if total > 0:
            pct = min(100, blocos * tam_bloco * 100 // total)
            print(f"\r  {pct}%", end="", flush=True)

    urllib.request.urlretrieve(URL, destino, progresso)
    print("\nDownload concluído.")


def main() -> None:
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--zip", default="dados/ml-25m.zip", help="caminho do ml-25m.zip (baixa se não existir)")
    ap.add_argument("--saida", default="dados/amostra.csv")
    ap.add_argument("--n", type=int, default=1_000_000, help="tamanho da amostra")
    ap.add_argument("--semente", type=int, default=2026)
    a = ap.parse_args()

    caminho = Path(a.zip)
    if not caminho.exists():
        caminho.parent.mkdir(parents=True, exist_ok=True)
        baixar(caminho)

    print("Lendo ratings.csv do zip...")
    with zipfile.ZipFile(caminho) as z:
        with z.open(MEMBRO) as f:
            df = pd.read_csv(f, dtype={"userId": "int32", "movieId": "int32",
                                       "rating": "float32", "timestamp": "int64"})
    print(f"{len(df):,} avaliações lidas.")

    if len(df) < a.n:
        print(f"ERRO: o arquivo tem menos de {a.n:,} registros.")
        sys.exit(1)

    # sample() já devolve as linhas em ordem aleatória, controlada pela semente
    amostra = df.sample(n=a.n, random_state=a.semente).reset_index(drop=True)

    Path(a.saida).parent.mkdir(parents=True, exist_ok=True)
    amostra.to_csv(a.saida, index=False)

    print(f"\n{len(amostra):,} avaliações gravadas em {a.saida} (semente {a.semente}).")
    print(f"timestamp: de {amostra.timestamp.min()} a {amostra.timestamp.max()}, "
          f"{amostra.timestamp.nunique():,} valores distintos")
    print(f"rating: {amostra.rating.nunique()} valores distintos")
    print(amostra.rating.value_counts().sort_index().to_string())


if __name__ == "__main__":
    main()

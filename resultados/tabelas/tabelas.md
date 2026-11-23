# Tabelas de resultados

Geradas por `scripts/gerar_graficos.py` a partir de `resultados/tabelas/resultados.csv`.


### Tempo de ordenação (ms), chave timestamp, n = 10.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 0,46 ± 0,13 | 263,0 ± 24,5 | 102,0 ± 0,93 |
| Selection Sort | 217,0 ± 0,96 | 207,5 ± 61,7 | 136,1 ± 10,1 |
| Quick Sort (pivô: primeiro) | 138,3 ± 4,17 | 203,0 ± 9,20 | 2,33 ± 0,42 |
| Quick Sort (pivô: mediana de três) | 1,06 ± 0,05 | 1,19 ± 0,10 | 2,20 ± 0,01 |
| Quick Sort (pivô: aleatório) | 1,43 ± 0,09 | 1,64 ± 0,09 | 2,50 ± 0,34 |
| Merge Sort | 1,11 ± 0,23 | 1,33 ± 0,24 | 2,54 ± 0,45 |

_Média ± desvio-padrão de 5 repetições. * = execução única (acima de 20 s não se repete). > 120 s = execução interrompida no limite de tempo. _

### Tempo de ordenação (ms), chave timestamp, n = 100.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 2,35 ± 0,30 | 30.615* | 14.988 ± 96,4 |
| Selection Sort | 23.448* | 23.007* | 19.109 ± 958,4 |
| Quick Sort (pivô: primeiro) | 21.787* | 29.835* | 26,6 ± 0,71 |
| Quick Sort (pivô: mediana de três) | 12,1 ± 0,05 | 13,2 ± 0,32 | 26,1 ± 0,49 |
| Quick Sort (pivô: aleatório) | 18,2 ± 0,29 | 19,5 ± 0,96 | 27,9 ± 0,54 |
| Merge Sort | 11,9 ± 0,36 | 15,6 ± 0,21 | 29,5 ± 0,69 |

_Média ± desvio-padrão de 5 repetições. * = execução única (acima de 20 s não se repete). > 120 s = execução interrompida no limite de tempo. _

### Tempo de ordenação (ms), chave timestamp, n = 1.000.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 33,9 ± 0,30 | > 120 s | > 120 s |
| Selection Sort | > 120 s | > 120 s | > 120 s |
| Quick Sort (pivô: primeiro) | > 120 s | > 120 s | 311,6 ± 2,46 |
| Quick Sort (pivô: mediana de três) | 130,8 ± 36,1 | 142,9 ± 2,36 | 319,8 ± 2,10 |
| Quick Sort (pivô: aleatório) | 164,9 ± 1,00 | 190,7 ± 1,52 | 324,8 ± 0,75 |
| Merge Sort | 216,2 ± 7,05 | 265,3 ± 1,34 | 444,8 ± 6,66 |

_Média ± desvio-padrão de 5 repetições. * = execução única (acima de 20 s não se repete). > 120 s = execução interrompida no limite de tempo. _

### Tempo de ordenação (ms), chave nota, n = 10.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 0,21 ± 0,02 | 163,8 ± 0,78 | 77,9 ± 0,34 |
| Selection Sort | 127,1 ± 2,59 | 127,9 ± 1,26 | 114,8 ± 1,14 |
| Quick Sort (pivô: primeiro) | 2,51 ± 0,09 | 2,04 ± 0,03 | 1,47 ± 0,01 |
| Quick Sort (pivô: mediana de três) | 1,57 ± 0,07 | 1,54 ± 0,11 | 1,66 ± 0,01 |
| Quick Sort (pivô: aleatório) | 1,29 ± 0,02 | 1,41 ± 0,08 | 1,57 ± 0,02 |
| Merge Sort | 0,85 ± 0,03 | 0,92 ± 0,06 | 1,45 ± 0,01 |

_Média ± desvio-padrão de 5 repetições. * = execução única (acima de 20 s não se repete). > 120 s = execução interrompida no limite de tempo. _

### Tempo de ordenação (ms), chave nota, n = 100.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 2,49 ± 0,72 | 23.101* | 11.263 ± 152,4 |
| Selection Sort | 22.055* | 21.118* | 13.934 ± 63,4 |
| Quick Sort (pivô: primeiro) | 40,8 ± 0,19 | 33,3 ± 1,37 | 17,8 ± 0,56 |
| Quick Sort (pivô: mediana de três) | 22,2 ± 0,07 | 19,4 ± 0,09 | 18,8 ± 0,08 |
| Quick Sort (pivô: aleatório) | 16,4 ± 0,36 | 16,6 ± 0,07 | 18,4 ± 0,16 |
| Merge Sort | 11,7 ± 0,47 | 12,1 ± 0,19 | 17,4 ± 0,23 |

_Média ± desvio-padrão de 5 repetições. * = execução única (acima de 20 s não se repete). > 120 s = execução interrompida no limite de tempo. _

### Tempo de ordenação (ms), chave nota, n = 1.000.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 21,2 ± 0,10 | > 120 s | > 120 s |
| Selection Sort | > 120 s | > 120 s | > 120 s |
| Quick Sort (pivô: primeiro) | 874,2 ± 13,3 | 666,5 ± 13,1 | 309,4 ± 37,8 |
| Quick Sort (pivô: mediana de três) | 305,2 ± 3,57 | 276,3 ± 1,06 | 346,8 ± 68,1 |
| Quick Sort (pivô: aleatório) | 233,9 ± 2,48 | 243,7 ± 3,30 | 293,3 ± 10,2 |
| Merge Sort | 164,6 ± 2,88 | 189,5 ± 5,03 | 256,0 ± 9,37 |

_Média ± desvio-padrão de 5 repetições. * = execução única (acima de 20 s não se repete). > 120 s = execução interrompida no limite de tempo. _

### Comparações entre chaves, chave timestamp, n = 10.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 9.999 | 49.995.000 | 25.079.120 |
| Selection Sort | 49.995.000 | 49.995.000 | 49.995.000 |
| Quick Sort (pivô: primeiro) | 50.015.003 | 50.000.339 | 218.946 |
| Quick Sort (pivô: mediana de três) | 171.652 | 171.665 | 221.057 |
| Quick Sort (pivô: aleatório) | 183.282 | 195.233 | 194.849 |
| Merge Sort | 69.008 | 64.612 | 120.410 |

_— = execução interrompida (contagem incompleta, não comparável)._

### Comparações entre chaves, chave timestamp, n = 100.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 99.999 | 4.999.949.982 | 2.509.842.849 |
| Selection Sort | 4.999.950.000 | 4.999.950.000 | 4.999.950.000 |
| Quick Sort (pivô: primeiro) | 4.999.887.741 | 4.979.847.622 | 2.657.217 |
| Quick Sort (pivô: mediana de três) | 2.047.510 | 2.047.529 | 2.613.307 |
| Quick Sort (pivô: aleatório) | 2.540.017 | 2.534.509 | 2.434.960 |
| Merge Sort | 853.904 | 815.271 | 1.536.417 |

_— = execução interrompida (contagem incompleta, não comparável)._

### Comparações entre chaves, chave timestamp, n = 1.000.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 999.999 | — | — |
| Selection Sort | — | — | — |
| Quick Sort (pivô: primeiro) | — | — | 32.228.973 |
| Quick Sort (pivô: mediana de três) | 23.848.387 | 23.848.370 | 30.570.702 |
| Quick Sort (pivô: aleatório) | 28.557.780 | 28.349.821 | 29.766.311 |
| Merge Sort | 10.066.432 | 9.901.311 | 18.673.976 |

_— = execução interrompida (contagem incompleta, não comparável)._

### Comparações entre chaves, chave nota, n = 10.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 9.999 | 41.944.905 | 20.688.911 |
| Selection Sort | 49.995.000 | 49.995.000 | 49.995.000 |
| Quick Sort (pivô: primeiro) | 695.014 | 504.149 | 160.467 |
| Quick Sort (pivô: mediana de três) | 237.555 | 217.671 | 185.134 |
| Quick Sort (pivô: aleatório) | 162.911 | 156.347 | 157.933 |
| Merge Sort | 69.008 | 74.069 | 114.662 |

_— = execução interrompida (contagem incompleta, não comparável)._

### Comparações entre chaves, chave nota, n = 100.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 99.999 | 4.189.111.268 | 2.083.985.725 |
| Selection Sort | 4.999.950.000 | 4.999.950.000 | 4.999.950.000 |
| Quick Sort (pivô: primeiro) | 9.194.559 | 6.563.215 | 1.939.256 |
| Quick Sort (pivô: mediana de três) | 3.171.907 | 2.773.819 | 2.189.602 |
| Quick Sort (pivô: aleatório) | 2.078.158 | 1.972.618 | 1.944.931 |
| Merge Sort | 853.904 | 963.029 | 1.455.294 |

_— = execução interrompida (contagem incompleta, não comparável)._

### Comparações entre chaves, chave nota, n = 1.000.000

| Algoritmo | Ordenado | Reverso | Aleatório |
|---|---:|---:|---:|
| Insertion Sort | 999.999 | — | — |
| Selection Sort | — | — | — |
| Quick Sort (pivô: primeiro) | 114.630.355 | 81.850.764 | 22.550.711 |
| Quick Sort (pivô: mediana de três) | 35.794.346 | 32.120.205 | 25.066.370 |
| Quick Sort (pivô: aleatório) | 22.531.691 | 22.564.719 | 22.025.735 |
| Merge Sort | 10.066.432 | 10.853.908 | 17.613.034 |

_— = execução interrompida (contagem incompleta, não comparável)._

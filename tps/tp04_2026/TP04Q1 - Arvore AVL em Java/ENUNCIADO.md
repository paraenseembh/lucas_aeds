# TP04 Q1 — Árvore AVL em Java

## Descrição

Implemente uma **Árvore AVL** (Adelson-Velsky e Landis) em Java para armazenar e buscar registros de restaurantes. A árvore deve se manter balanceada automaticamente após cada inserção, garantindo altura O(log n).

Os restaurantes são lidos de um arquivo CSV em `/tmp/restaurantes.csv`. O programa recebe via entrada padrão os **IDs** dos restaurantes a inserir, seguidos de **nomes** a buscar na árvore.

---

## Estrutura do Arquivo CSV

```
id,nome,cidade,capacidade,avaliacao,tipos_cozinha,faixa_preco,horario,data_abertura,aberto
1,Classic Palace Works,Zurich,168,3.9,churrasco;internacional,$$,11:00-20:00,2018-03-31,false
```

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | inteiro | Identificador único |
| `nome` | string | Nome do restaurante |
| `cidade` | string | Cidade |
| `capacidade` | inteiro | Capacidade de pessoas |
| `avaliacao` | real | Nota de 0.0 a 5.0 |
| `tipos_cozinha` | string | Tipos separados por `;` |
| `faixa_preco` | string | `$`, `$$`, `$$$` ou `$$$$` |
| `horario` | string | Formato `HH:MM-HH:MM` |
| `data_abertura` | string | Formato `YYYY-MM-DD` |
| `aberto` | booleano | `true` ou `false` |

---

## Entrada

```
<id1>
<id2>
...
-1
<nome1>
<nome2>
...
FIM
```

- Os **IDs** são lidos até encontrar `-1`. Cada ID corresponde a um restaurante do CSV que deve ser inserido na AVL (chave = nome do restaurante).
- Os **nomes** são lidos até encontrar `FIM`. Cada nome é buscado na árvore AVL.

---

## Saída

Para cada nome buscado, imprimir o **caminho percorrido** da raiz até o nó, com os passos `esq` ou `dir`, terminando em `SIM` (encontrado) ou `NAO` (não encontrado):

```
raiz esq dir esq SIM
raiz dir dir NAO
```

Em seguida, imprimir todos os restaurantes em **ordem crescente de nome** (percurso in-order), um por linha, no formato:

```
[<id> ## <nome> ## <cidade> ## <capacidade> ## <avaliacao> ## [<tipo1>,<tipo2>] ## <faixa_preco> ## <abertura>-<fechamento> ## <dd/mm/yyyy> ## <aberto>]
```

---

## Exemplo

### Entrada
```
7
10
12
-1
Classic Palace Works
Sunny Garden Studio
Grand Bistro Collective
FIM
```

### Saída
```
raiz esq dir esq esq dir esq NAO
raiz dir dir dir esq esq dir NAO
raiz SIM
[7 ## Grand Bistro Collective ## Tokyo ## 49 ## 4.4 ## [japonesa,sushi] ## $ ## 08:00-23:00 ## 12/04/2024 ## false]
[10 ## Grand Spot Works 61 ## Beijing ## 43 ## 4.3 ## [chinesa,asiatica] ## $$$ ## 09:00-22:00 ## 09/03/2018 ## true]
[12 ## Elite Place Works 21 ## Beijing ## 69 ## 4.9 ## [chinesa,asiatica] ## $$$ ## 09:00-23:00 ## 03/03/2019 ## false]
```

---

## Requisitos da Árvore AVL

1. **Inserção**: inserir pelo nome do restaurante como chave.
2. **Balanceamento**: após cada inserção, calcular o **fator de balanço** (altura da subárvore esquerda − altura da subárvore direita) de cada ancestral.
3. **Rotações**: se o fator de balanço ficar fora do intervalo `[-1, 1]`, aplicar a rotação adequada:
   - **LL** → rotação simples à direita
   - **RR** → rotação simples à esquerda
   - **LR** → rotação dupla: esquerda no filho, depois direita no nó
   - **RL** → rotação dupla: direita no filho, depois esquerda no nó
4. **Busca**: percorrer a árvore exatamente como em uma BST, registrando o caminho.
5. **In-order**: percurso em-ordem retorna os restaurantes em ordem alfabética pelo nome.

---

## Arquivo de Log

Ao final, gerar o arquivo `797798_avl.txt` com o formato:

```
<matricula>\t<comparacoes>\t<movimentacoes>\t<tempo>ms
```

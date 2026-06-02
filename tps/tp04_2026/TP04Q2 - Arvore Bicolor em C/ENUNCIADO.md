# TP04 Q2 — Árvore Bicolor (Red-Black Tree) em C

## Descrição

Implemente uma **Árvore Bicolor** (Red-Black Tree) em C para armazenar e buscar registros de restaurantes. A árvore deve se manter balanceada automaticamente por meio das propriedades de coloração vermelho/preto após cada inserção.

Os restaurantes são lidos de um arquivo CSV em `/tmp/restaurantes.csv`. O programa recebe via entrada padrão os **IDs** dos restaurantes a inserir, seguidos de **nomes** a buscar na árvore.

---

## Propriedades da Árvore Bicolor

1. Todo nó é **vermelho** ou **preto**.
2. A **raiz** é sempre preta.
3. Todo **nó nulo** (sentinela NIL) é preto.
4. Se um nó é **vermelho**, ambos os filhos são pretos (sem dois vermelhos consecutivos).
5. Para qualquer nó, todos os caminhos simples até as folhas descendentes contêm o mesmo número de **nós pretos** (altura negra uniforme).

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

- Os **IDs** são lidos até encontrar `-1`. Cada ID corresponde a um restaurante do CSV que deve ser inserido na RBT (chave = nome do restaurante).
- Os **nomes** são lidos até encontrar `FIM`. Cada nome é buscado na árvore.

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

## Algoritmo de Inserção (Fix-Up)

Após inserir o novo nó como **vermelho**, corrigir violações das propriedades:

| Caso | Condição | Ação |
|------|----------|------|
| 1 | Tio é vermelho | Recolorir pai, tio → preto; avô → vermelho; subir |
| 2 | Tio é preto, nó é filho interno | Rotacionar o pai (transformar em caso 3) |
| 3 | Tio é preto, nó é filho externo | Recolorir pai/avô + rotacionar avô |

Os casos são simétricos para inserções à esquerda ou à direita.

---

## Arquivo de Log

Ao final, gerar o arquivo `797798_rbt.txt` com o formato:

```
<matricula>\t<comparacoes>\t<movimentacoes>\t<tempo>ms
```

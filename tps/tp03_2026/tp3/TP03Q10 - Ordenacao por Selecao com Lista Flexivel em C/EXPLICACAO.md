# TP03Q10 — Ordenação por Seleção com Lista Flexível em C

## O que o problema pede

Dado um conjunto de IDs de restaurantes (lidos de `stdin` até `-1`), ordenar esses restaurantes **pelo nome** usando o algoritmo **Seleção (Selection Sort)**, mas armazenando os elementos em uma **lista encadeada simples (lista flexível)** — não em um array estático.

A saída é cada restaurante no formato:
```
[id ## nome ## cidade ## capacidade ## avaliacao ## [tipos] ## faixa ## abertura-fechamento ## data ## aberto]
```

Ao final, um arquivo de log (`797798_selecao.txt`) é escrito com:
```
<matricula>  <comparacoes>  <movimentacoes>  <tempo_ms>
```

---

## Como resolver

### 1. Estrutura da Lista Flexível

Uma lista encadeada simples composta por nós, onde cada nó guarda um ponteiro para o restaurante e um ponteiro para o próximo nó:

```c
typedef struct No {
    Restaurante* dado;
    struct No* proximo;
} No;

typedef struct {
    No* cabeca;
    int tamanho;
} Lista;
```

A inserção no fim percorre a lista até o último nó e encadeia o novo:

```c
void inserir_fim(Lista* l, Restaurante* r) {
    No* novo = malloc(sizeof(No));
    novo->dado = r;
    novo->proximo = NULL;
    if (!l->cabeca) { l->cabeca = novo; return; }
    No* atual = l->cabeca;
    while (atual->proximo) atual = atual->proximo;
    atual->proximo = novo;
    l->tamanho++;
}
```

### 2. Selection Sort na Lista Encadeada

A ideia é a mesma do sort em array: para cada posição `i`, encontra o menor elemento a partir de `i` e troca com o elemento em `i`. Na lista, "posição `i`" é um nó; a troca é feita **trocando os ponteiros `dado`** dos nós (mais simples do que re-encadear nós):

```c
void selecao_lista(Lista* l) {
    for (No* i = l->cabeca; i != NULL; i = i->proximo) {
        No* minimo = i;
        for (No* j = i->proximo; j != NULL; j = j->proximo) {
            comparacoes++;
            if (strcmp(j->dado->nome, minimo->dado->nome) < 0)
                minimo = j;
        }
        if (minimo != i) {
            Restaurante* tmp = i->dado;
            i->dado = minimo->dado;
            minimo->dado = tmp;
            movimentacoes += 3;  /* 3 atribuicoes por troca */
        }
    }
}
```

**Complexidade:** O(n²) comparações, O(n) trocas no pior caso — igual ao sort em array.

### 3. Fluxo principal

```
1. Ler CSV de restaurantes em um vetor de busca
2. Ler IDs do stdin → para cada ID, busca no vetor e insere na lista
3. Executar selecao_lista(lista)
4. Imprimir cada nó formatado
5. Gravar log de métricas
```

---

## Por que trocar `dado` e não re-encadear os nós?

Trocar os ponteiros internos `dado` é **O(1) por troca** e evita ter que atualizar os ponteiros `proximo` dos nós adjacentes, o que tornaria o código muito mais complexo sem ganho de desempenho.

---

## Possíveis variações

### Variação 1 — Critério de ordenação diferente

Basta mudar o `strcmp` por outra comparação:

```c
/* Por avaliacao decrescente */
if (j->dado->avaliacao > minimo->dado->avaliacao)
    minimo = j;

/* Por capacidade crescente */
if (j->dado->capacidade < minimo->dado->capacidade)
    minimo = j;

/* Por data de abertura (mais antigo primeiro) */
if (j->dado->data_abertura.ano < minimo->dado->data_abertura.ano)
    minimo = j;
```

### Variação 2 — Inserção ordenada (evitar sort posterior)

Em vez de inserir no fim e ordenar depois, inserir cada elemento **já na posição correta** mantendo a lista sempre ordenada (custo O(n) por inserção, O(n²) total — mesma complexidade, mas útil para inserções incrementais):

```c
void inserir_ordenado(Lista* l, Restaurante* r) {
    No* novo = malloc(sizeof(No));
    novo->dado = r;
    if (!l->cabeca || strcmp(r->nome, l->cabeca->dado->nome) < 0) {
        novo->proximo = l->cabeca;
        l->cabeca = novo;
        return;
    }
    No* atual = l->cabeca;
    while (atual->proximo && strcmp(r->nome, atual->proximo->dado->nome) >= 0)
        atual = atual->proximo;
    novo->proximo = atual->proximo;
    atual->proximo = novo;
}
```

### Variação 3 — Lista duplamente encadeada

Adicionar um ponteiro `anterior` em cada nó permite percorrer a lista nos dois sentidos, útil para algoritmos como Insertion Sort que precisam "voltar" na lista:

```c
typedef struct No {
    Restaurante* dado;
    struct No* proximo;
    struct No* anterior;
} No;
```

### Variação 4 — Ordenação estável com Insertion Sort na lista

Selection Sort **não é estável** (elementos iguais podem ter a ordem relativa trocada). Para estabilidade, use Insertion Sort na lista:

```c
void insercao_lista(Lista* l) {
    if (!l->cabeca) return;
    No* ordenado = l->cabeca;
    No* atual = ordenado->proximo;
    ordenado->proximo = NULL;

    while (atual) {
        No* proximo = atual->proximo;
        if (strcmp(atual->dado->nome, ordenado->nome) <= 0) {
            atual->proximo = ordenado;
            ordenado = atual;
        } else {
            No* tmp = ordenado;
            while (tmp->proximo && strcmp(atual->dado->nome, tmp->proximo->dado->nome) > 0)
                tmp = tmp->proximo;
            atual->proximo = tmp->proximo;
            tmp->proximo = atual;
        }
        atual = proximo;
    }
    l->cabeca = ordenado;
}
```

### Variação 5 — Liberação de memória

Para completude em produção, liberar os nós ao final:

```c
void destruir_lista(Lista* l) {
    No* atual = l->cabeca;
    while (atual) {
        No* prox = atual->proximo;
        free(atual);
        atual = prox;
    }
    free(l);
}
```

---

## Análise de Complexidade

| Aspecto              | Selection Sort (lista ou array) |
|----------------------|---------------------------------|
| Comparações          | O(n²) — sempre                 |
| Trocas (pior caso)   | O(n)                            |
| Trocas (melhor caso) | O(0) — lista já ordenada       |
| Espaço extra         | O(1) — in-place                 |
| Estabilidade         | Não estável                     |

A diferença entre usar lista e array é apenas de **constante de alocação** (cada nó tem overhead do ponteiro `proximo`), sem mudar a complexidade assintótica.

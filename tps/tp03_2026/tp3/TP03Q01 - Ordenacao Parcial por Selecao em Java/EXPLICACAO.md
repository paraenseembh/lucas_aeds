# TP03Q01 — Ordenação Parcial por Seleção em Java

## O que o problema pede

Dado um conjunto de n IDs de restaurantes (lidos de `stdin` até `-1`), executar **k = n/5 passes** do algoritmo Selection Sort ordenando pelo **nome**, e então imprimir **todos os n elementos** (os primeiros k estarão em ordem, o restante ficará no estado resultante das trocas).

A saída de cada restaurante segue o formato:
```
[id ## nome ## cidade ## capacidade ## avaliacao ## [tipos] ## faixa ## abertura-fechamento ## data ## aberto]
```

Ao final, um arquivo `797798_selecao.txt` é gravado com:
```
<matricula>  <comparacoes>  <movimentacoes>  <tempo_s>
```

---

## Diferença entre Ordenação Total e Parcial

| Característica     | Ordenação Total          | Ordenação Parcial (esta questão) |
|--------------------|--------------------------|----------------------------------|
| Passes executados  | n − 1                    | k = n / 5                        |
| Elementos ordenados| Todos os n               | Apenas os k primeiros            |
| Demais elementos   | —                        | Estado intermediário das trocas  |
| Complexidade       | O(n²)                    | O(k · n) = O(n²/5)              |

---

## Como resolver

### 1. Carregar o CSV e montar o array

```java
BufferedReader csv = new BufferedReader(new FileReader("/tmp/restaurantes.csv"));
csv.readLine(); // pula cabecalho
Map<Integer, Restaurante> mapa = new HashMap<>();
String line;
while ((line = csv.readLine()) != null)
    if (!line.trim().isEmpty()) {
        Restaurante r = Restaurante.parseRestaurante(line);
        mapa.put(r.getId(), r);
    }
```

Usar um `Map<Integer, Restaurante>` para busca O(1) por ID é muito mais eficiente do que percorrer um array a cada ID lido.

### 2. Seleção Parcial

```java
static void selecaoParcial(Restaurante[] arr, int n) {
    int k = n / 5;                          // apenas k passes
    for (int i = 0; i < k; i++) {
        int min = i;
        for (int j = i + 1; j < n; j++) {
            comparacoes++;
            if (arr[j].getNome().compareTo(arr[min].getNome()) < 0) min = j;
        }
        if (min != i) {
            Restaurante tmp = arr[i]; arr[i] = arr[min]; arr[min] = tmp;
            movimentacoes += 3;
        }
    }
}
```

A única diferença em relação ao Selection Sort completo é o limite do laço externo: `i < k` em vez de `i < n - 1`.

### 3. Fluxo principal

```
1. Ler CSV → HashMap<id, Restaurante>
2. Ler IDs do stdin → construir array
3. Calcular k = n / 5
4. Executar k passes do Selection Sort por nome
5. Imprimir todos os n elementos
6. Gravar log de métricas
```

---

## Invariante do Selection Sort

Após o i-ésimo passe, `arr[0..i]` contém os i+1 menores elementos (por nome) em ordem crescente, e esses elementos **nunca mais são movidos**. O restante do array permanece em estado parcialmente alterado pelas trocas anteriores.

---

## Possíveis variações

### Variação 1 — Critério diferente de ordenação

```java
// Por avaliação decrescente (maior primeiro)
if (arr[j].getAvaliacao() > arr[min].getAvaliacao()) min = j;

// Por capacidade crescente
if (arr[j].getCapacidade() < arr[min].getCapacidade()) min = j;

// Por data de abertura (mais antigo primeiro)
if (arr[j].getDataAbertura().compareTo(arr[min].getDataAbertura()) < 0) min = j;
```

### Variação 2 — k diferente de n/5

O valor de k pode ser qualquer inteiro 1 ≤ k ≤ n. Casos típicos em problemas acadêmicos:

| k          | Resultado                             |
|------------|---------------------------------------|
| k = 1      | Apenas o menor elemento na posição 0  |
| k = n/5    | 20% dos elementos ordenados (este TP) |
| k = n/2    | Primeira metade ordenada              |
| k = n − 1  | Ordenação completa                    |

### Variação 3 — Ordenação decrescente (máximo em vez de mínimo)

```java
for (int i = 0; i < k; i++) {
    int max = i;
    for (int j = i + 1; j < n; j++) {
        comparacoes++;
        if (arr[j].getNome().compareTo(arr[max].getNome()) > 0) max = j;
    }
    if (max != i) {
        Restaurante tmp = arr[i]; arr[i] = arr[max]; arr[max] = tmp;
        movimentacoes += 3;
    }
}
```

### Variação 4 — Usando Comparable / Comparator (código mais genérico)

```java
static <T extends Comparable<T>> void selecaoParcial(T[] arr, int n) {
    int k = n / 5;
    for (int i = 0; i < k; i++) {
        int min = i;
        for (int j = i + 1; j < n; j++)
            if (arr[j].compareTo(arr[min]) < 0) min = j;
        if (min != i) { T tmp = arr[i]; arr[i] = arr[min]; arr[min] = tmp; }
    }
}
```

Ou com `Comparator` para critérios plugáveis:

```java
static <T> void selecaoParcial(T[] arr, int n, Comparator<T> cmp) {
    int k = n / 5;
    for (int i = 0; i < k; i++) {
        int min = i;
        for (int j = i + 1; j < n; j++)
            if (cmp.compare(arr[j], arr[min]) < 0) min = j;
        if (min != i) { T tmp = arr[i]; arr[i] = arr[min]; arr[min] = tmp; }
    }
}
// Uso: selecaoParcial(arr, n, Comparator.comparing(Restaurante::getNome));
```

---

## Análise de Complexidade

| Aspecto            | Selection Sort Total | Selection Sort Parcial (k = n/5) |
|--------------------|----------------------|-----------------------------------|
| Comparações        | n(n−1)/2 ≈ O(n²)    | k(n−k) + k(k−1)/2 ≈ O(k·n)      |
| Trocas (pior caso) | n − 1 = O(n)         | k = n/5 = O(n)                    |
| Espaço extra       | O(1)                 | O(1)                              |
| Estabilidade       | Não estável          | Não estável                       |

Com k = n/5, o número de comparações é aproximadamente **1/5 do total** do sort completo, tornando a versão parcial significativamente mais rápida quando apenas os k menores elementos importam.

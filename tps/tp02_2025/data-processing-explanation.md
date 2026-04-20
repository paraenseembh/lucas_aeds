# Lógica de Processamento de Dados do Disney+

Este documento explica a lógica de processamento de dados implementada na nossa solução Quicksort para o conjunto de dados do Disney+. A solução organiza os shows pelo atributo `date_added` com `title` como critério de desempate.

## Índice
1. [Estrutura de Dados](#estrutura-de-dados)
2. [Análise de CSV](#análise-de-csv)
3. [Processamento de Arrays de Strings](#processamento-de-arrays-de-strings)
4. [Processamento de Datas](#processamento-de-datas)
5. [Lógica de Comparação](#lógica-de-comparação)
6. [Implementação da Ordenação](#implementação-da-ordenação)

## Estrutura de Dados

A estrutura `Show` armazena todas as informações relevantes do arquivo CSV:

```c
typedef struct Show {
    char *show_id;
    char *type;
    char *title;
    char *director;
    char *cast;
    char *country;
    char *date_added;
    char *release_year;
    char *rating;
    char *duration;
    char *listed_in;
    char *description;
    char **cast_array;    // Elenco processado e ordenado
    char **categories;    // Categorias processadas e ordenadas
    int cast_size;        // Número de membros do elenco
    int categories_size;  // Número de categorias
    int day, month, year; // Componentes da data extraídos
    int has_date;         // Flag indicando data válida
} Show;
```

## Análise de CSV

A análise do CSV é complexa porque precisa lidar com:
- Campos com vírgulas dentro de texto entre aspas
- Valores ausentes (representados como "NaN")
- Alocação adequada de memória para campos de tamanho dinâmico

### Extração de Campos

A função `extractField` extrai campos individuais da linha CSV:

```c
char *extractField(char *next, int *extracted) {
    char *field = malloc(1000);
    int i=0, j=0, continue_extract = 1, in_quotes = 0, characters = 0;

    while(continue_extract && next[i] != '\0') {
        if(!in_quotes) {
            if(next[i] == ',') {
                continue_extract = !continue_extract;
            } else {
                if(next[i] == '"') {
                    in_quotes = !in_quotes;
                } else {
                    field[j++] = next[i];
                    characters++;
                }
            }
        } else {
            if(next[i] == '"') {
                in_quotes = !in_quotes;
            } else {
                field[j++] = next[i];
                characters++;
            }
        }
        i++;
    }
    
    // Trata campos vazios substituindo por "NaN"
    if(characters == 0) {
        field[j++] = 'N';
        field[j++] = 'a';
        field[j++] = 'N';
    }
    field[j] = '\0';
    *extracted = i;

    return field;
}
```

Esta função funciona:
1. Lendo a entrada caractere por caractere
2. Rastreando se estamos dentro de texto entre aspas (para lidar com vírgulas dentro de aspas)
3. Construindo o valor do campo até encontrar uma vírgula fora das aspas
4. Definindo campos vazios como "NaN"
5. Retornando o número de caracteres processados para avançar o ponteiro de entrada

## Processamento de Arrays de Strings

Os campos `cast` e `listed_in` contêm listas separadas por vírgulas que precisam ser:
1. Divididas em strings individuais
2. Removidos os espaços em branco
3. Ordenadas alfabeticamente (conforme exigido)

A função `splitString` realiza este processo:

```c
char **splitString(char *line, int *size) {
    if (line == NULL || strcmp(line, "NaN") == 0) {
        *size = 0;
        return NULL;
    }

    // Conta vírgulas para determinar tamanho do array
    int i = 0, j = 0, k = 0;
    *size = 1;
    for (i = 0; line[i] != '\0'; i++) {
        if (line[i] == ',') {
            (*size)++;
        }
    }

    // Aloca memória para o array de strings
    char **result = (char **)malloc(sizeof(char*) * (*size));
    for (i = 0; i < *size; i++) {
        result[i] = NULL;
    }

    // Divide a string por vírgulas
    i = j = k = 0;
    result[k] = (char *)malloc(sizeof(char) * (strlen(line) + 1));
    while (line[i] != '\0') {
        if (line[i] == ',') {
            result[k][j] = '\0';
            trim(result[k]);
            k++;
            j = 0;
            result[k] = (char *)malloc(sizeof(char) * (strlen(line) + 1));
        } else if (line[i] != '"') {
            result[k][j++] = line[i];
        }
        i++;
    }
    result[k][j] = '\0';
    trim(result[k]);

    // Ordena o array alfabeticamente usando selection sort
    for (int m = 0; m < *size - 1; m++) {
        int smallest = m;
        for (int n = m + 1; n < *size; n++) {
            if (strcmp(result[n], result[smallest]) < 0) {
                smallest = n;
            }
        }
        if (smallest != m) {
            char *temp = result[m];
            result[m] = result[smallest];
            result[smallest] = temp;
        }
    }

    return result;
}
```

Esta implementação:
1. Trata entrada NULL ou "NaN" adequadamente
2. Divide a string por vírgulas enquanto lida com texto entre aspas
3. Aplica a função `trim` para remover espaços no início/fim
4. Ordena o array alfabeticamente usando um selection sort simples

## Processamento de Datas

O processamento de datas é crucial para o requisito de ordenação. O campo `date_added` vem em um formato como "Mês dia, ano" (ex.: "October 1, 2019").

A função `processDate` extrai os componentes em valores numéricos:

```c
void processDate(Show *s) {
    if (s == NULL || strcmp(s->date_added, "NaN") == 0) {
        s->has_date = 0;
        return;
    }

    char month[20] = {0};
    int day = 0, year = 0;

    if (sscanf(s->date_added, "%s %d, %d", month, &day, &year) == 3) {
        s->month = monthToInt(month);
        s->day = day;
        s->year = year;
        s->has_date = (s->month > 0) ? 1 : 0;
    } else {
        s->has_date = 0;
    }
}
```

A função `monthToInt` converte nomes de meses em números:

```c
int monthToInt(char *month) {
    if (month == NULL) return 0;
    
    if (strstr(month, "January")) return 1;
    if (strstr(month, "February")) return 2;
    if (strstr(month, "March")) return 3;
    if (strstr(month, "April")) return 4;
    if (strstr(month, "May")) return 5;
    if (strstr(month, "June")) return 6;
    if (strstr(month, "July")) return 7;
    if (strstr(month, "August")) return 8;
    if (strstr(month, "September")) return 9;
    if (strstr(month, "October")) return 10;
    if (strstr(month, "November")) return 11;
    if (strstr(month, "December")) return 12;
    return 0;
}
```

Usar `strstr` em vez de comparação direta de strings torna a detecção de meses mais robusta, lidando com possíveis variações no formato.

## Lógica de Comparação

A lógica de comparação para ordenação é implementada na função `compareShows`:

```c
int compareShows(const Show *a, const Show *b, int *comparisons) {
    (*comparisons)++;
    
    // Caso 1: Ambos não têm data
    if (!a->has_date && !b->has_date) {
        (*comparisons)++;
        return strcmp(a->title, b->title);
    }
    
    // Caso 2: a não tem data
    if (!a->has_date) {
        return 1; // a é maior (vai para o fim)
    }
    
    // Caso 3: b não tem data
    if (!b->has_date) {
        return -1; // a é menor (vai para o início)
    }
    
    // Caso 4: Ambos têm datas
    if (a->year != b->year) return a->year - b->year;
    if (a->month != b->month) return a->month - b->month;
    if (a->day != b->day) return a->day - b->day;
    
    // Datas iguais, ordena por título
    (*comparisons)++;
    return strcmp(a->title, b->title);
}
```

Esta função trata quatro casos distintos:
1. **Ambos os shows não têm data**: Usa o título como chave de ordenação
2. **Apenas o show A não tem data**: Coloca A depois de B (no final)
3. **Apenas o show B não tem data**: Coloca A antes de B (no início)
4. **Ambos os shows têm datas**: Compara por ano, depois mês, depois dia
5. **Datas iguais**: Usa o título como critério de desempate

A função também incrementa um contador para rastrear o número de comparações realizadas.

## Implementação da Ordenação

O passo final é a implementação do algoritmo Quicksort:

```c
void quicksort(Show *arr[], int left, int right, int *comparisons, int *movements) {
    if (left < right) {
        int i = left, j = right;
        Show *pivot = arr[(left + right) / 2];

        while (i <= j) {
            while (compareShows(arr[i], pivot, comparisons) < 0) i++;
            while (compareShows(arr[j], pivot, comparisons) > 0) j--;

            if (i <= j) {
                swapShows(&arr[i], &arr[j], movements);
                i++; j--;
            }
        }
        
        if (left < j) quicksort(arr, left, j, comparisons, movements);
        if (i < right) quicksort(arr, i, right, comparisons, movements);
    }
}
```

A implementação do Quicksort:
1. Usa o elemento do meio como pivô
2. Usa a função `compareShows` para determinar a ordenação
3. Rastreia tanto comparações quanto movimentações para análise de desempenho
4. Ordena sub-arrays recursivamente

A função `swapShows` trata da movimentação dos elementos:

```c
void swapShows(Show **a, Show **b, int *movements) {
    Show *temp = *a;
    *a = *b;
    *b = temp;
    (*movements) += 3; // Conta 3 movimentações (a→temp, b→a, temp→b)
}
```

Esta abordagem abrangente garante que o conjunto de dados do Disney+ seja corretamente analisado, processado e ordenado pelo atributo `date_added` com `title` como critério de desempate, mantendo toda a formatação necessária e tratando casos limites adequadamente.

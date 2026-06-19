# TP04Q7 - Árvore Binária de Lista em C

## Regras Básicas

1. extends Trabalho Prático 03
2. Neste trabalho, você deverá utilizar o Dataset fornecido no Trabalho Prático 02 como base para os exercícios. Para os exercícios que envolvem pesquisa, seu programa deve criar um arquivo de log na pasta corrente com o nome `matrícula_<algoritmo>.txt` com uma única linha contendo sua matrícula, o número de comparações de todas as pesquisas e o tempo de execução total das pesquisas. Todas as informações do arquivo de log devem ser separadas por uma tabulação `'\t'`.

## Estruturas Híbridas

### Questão 7 - Árvore Binária de Lista em C

Criar uma estrutura árvore de listas para armazenar e pesquisar Restaurantes, no estilo de uma Agenda.

- A árvore (primeiro nível) é uma Árvore Binária de Pesquisa com chave do tipo `char`, que armazena o primeiro caractere do nome do restaurante que está sendo inserido.
- A lista do segundo nível é uma lista simplesmente encadeada que armazena o ponteiro para o restaurante. A lista armazena os restaurantes **ordenados pelo nome**.

A entrada padrão segue o mesmo formato da questão "Árvore Binária em Java" do TP03. Na primeira parte da entrada, você deve inserir os restaurantes na estrutura de dados. Na segunda parte da entrada, você deve efetuar pesquisas a partir de nomes de restaurantes.

A saída padrão é composta por várias linhas, uma para cada pesquisa. Cada linha é composta pelo caminho de ponteiros utilizados na pesquisa e, no final, pelas palavras `SIM` ou `NAO`, indicando o resultado da pesquisa. Caso o restaurante seja encontrado, imprima, após a palavra `SIM`, o restaurante formatado.

O caminho de ponteiros é `RAIZ`, `ESQ` ou `DIR` para a árvore. Na pesquisa ao longo da lista, imprima o nome dos restaurantes percorridos.

**Nome do arquivo de log:** `matrícula_hibrida_arvore_lista.txt`

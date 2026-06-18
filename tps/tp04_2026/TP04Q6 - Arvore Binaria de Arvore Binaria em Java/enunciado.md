# TP04Q6 - Árvore Binária de Árvore Binária em Java

## Regras Básicas

1. extends Trabalho Prático 03
2. Neste trabalho, você deverá utilizar o Dataset fornecido no Trabalho Prático 02 como base para os exercícios. Para os exercícios que envolvem pesquisa, seu programa deve criar um arquivo de log na pasta corrente com o nome `matrícula_<algoritmo>.txt` com uma única linha contendo sua matrícula, o número de comparações de todas as pesquisas e o tempo de execução total das pesquisas. Todas as informações do arquivo de log devem ser separadas por uma tabulação `'\t'`.

## Estruturas Híbridas

### Questão 6 - Árvore Binária de Árvore Binária em Java

Criar uma estrutura árvore de árvores para armazenar e pesquisar Restaurantes.

- A árvore do **primeiro nível** é uma Árvore Binária de Pesquisa com chave do tipo inteiro, obtida pela operação `capacidade mod 15`, sendo utilizado o atributo capacidade do restaurante que está sendo inserido.
- A árvore do **segundo nível** é uma AVL cuja chave é o atributo nome do restaurante.

A entrada padrão segue o mesmo formato da questão "Árvore Binária em Java" do TP03. Na primeira parte da entrada, você deve inserir os restaurantes na estrutura de dados. Na segunda parte da entrada, você deve efetuar pesquisas a partir de nomes de restaurantes.

A saída padrão é composta por várias linhas, uma para cada pesquisa. Cada linha é composta pelo caminho de ponteiros utilizados na pesquisa e, no final, pelas palavras `SIM` ou `NAO`, indicando o resultado da pesquisa. Caso o restaurante seja encontrado, imprima, após a palavra `SIM`, o restaurante formatado.

O caminho de ponteiros é: `(RAIZ, ESQ ou DIR)` para a árvore do primeiro nível e `(raiz, esq ou dir)` para a árvore do segundo nível. Na pesquisa, tão logo chegue a um nó da árvore do primeiro nível, pesquise na árvore correspondente do segundo nível.

**Nome do arquivo de log:** `matrícula_hibrida_arvore_arvore.txt`

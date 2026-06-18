# TP04Q3 - Tabela Hash Direta com Reserva em C

## Regras Básicas

1. extends Trabalho Prático 03
2. Neste trabalho, você deverá utilizar o Dataset fornecido no Trabalho Prático 02 como base para os exercícios. Para os exercícios que envolvem pesquisa, seu programa deve criar um arquivo de log na pasta corrente com o nome `matrícula_<algoritmo>.txt` com uma única linha contendo sua matrícula, o número de comparações de todas as pesquisas e o tempo de execução total das pesquisas. Todas as informações do arquivo de log devem ser separadas por uma tabulação `'\t'`.

## Tabelas Hash

### Questão 3 - Tabela Hash Direta com Reserva em C

Refaça a questão "Árvore Binária em Java" do TP03 com Tabela Hash Direta com Reserva.

A função de transformação será `(ASCII_nome) mod tamTab`, onde `(ASCII_nome)` é a soma dos códigos ASCII do atributo nome e `tamTab` é o tamanho da tabela.

Considere que a tabela tenha tamanho **31** e a área de reserva tamanho **19**.

Durante a inserção, caso a inserção do restaurante não possa ser realizada por causa de colisão e falta de espaço na área de reserva, imprima o nome do restaurante.

A saída padrão será a posição de cada elemento procurado na tabela (na hash ou na área de reserva, isto é, de 0 a 49), seguido do restaurante formatado. Se o elemento procurado não estiver na tabela, escreva `-1`.

**Nome do arquivo de log:** `matrícula_hash_reserva.txt`

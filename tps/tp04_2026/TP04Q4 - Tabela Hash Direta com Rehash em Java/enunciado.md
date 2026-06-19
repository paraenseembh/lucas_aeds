# TP04Q4 - Tabela Hash Direta com Rehash em Java

## Regras Básicas

1. extends Trabalho Prático 03
2. Neste trabalho, você deverá utilizar o Dataset fornecido no Trabalho Prático 02 como base para os exercícios. Para os exercícios que envolvem pesquisa, seu programa deve criar um arquivo de log na pasta corrente com o nome `matrícula_<algoritmo>.txt` com uma única linha contendo sua matrícula, o número de comparações de todas as pesquisas e o tempo de execução total das pesquisas. Todas as informações do arquivo de log devem ser separadas por uma tabulação `'\t'`.

## Tabelas Hash

### Questão 4 - Tabela Hash Direta com Rehash em Java

Refaça a questão "Tabela Hash Direta com Reserva em C" com Tabela Hash Direta com Rehash.

- A primeira função de transformação será `(ASCII_nome) mod tamTab`, onde `tamTab` (tamanho da tabela) é **83**.
- A segunda função de transformação será `(ASCII_nome + 1) mod tamTab`.

**Nome do arquivo de log:** `matrícula_hash_rehash.txt`

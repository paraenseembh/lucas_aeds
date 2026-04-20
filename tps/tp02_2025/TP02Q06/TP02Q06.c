/**
 * Programa de manipulação e ordenação de shows do Disney+
 * @author Lucas Nascimento
 * Matrícula: 1404192
 */

 #include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 #include <time.h>
 
 // Constantes
 #define MAX_SHOWS 1368     // Número máximo de shows no arquivo
 #define TAM_LINHA 1000     // Tamanho máximo de cada linha do arquivo
 
 /**
  * Estrutura para armazenar informações de um show
  */
 typedef struct Show {
     char *show_id;         // Identificador único do show
     char *type;            // Tipo (Filme ou Série)
     char *title;           // Título
     char *director;        // Diretor
     char *cast;            // Elenco (string completa)
     char *country;         // País de origem
     char *date_added;      // Data adicionada ao catálogo
     char *release_year;    // Ano de lançamento
     char *rating;          // Classificação
     char *duration;        // Duração
     char *listed_in;       // Categorias (string completa)
     char *description;     // Descrição
     char **elenco;         // Array com atores do elenco
     char **listados;       // Array com categorias
     int tam_elenco;        // Tamanho do array elenco
     int tam_listados;      // Tamanho do array listados
     int dia, mes, ano;     // Componentes da data
     int tem_data;          // Flag de data válida
 } Show;
 
 /**
  * Obtém o tamanho de uma string
  * @param linha String a ser medida
  * @return Tamanho da string
  */
 int getTam(char* linha) {
     int tam = 0;
     while(linha[tam] != '\0') {
         tam++;
     }
     return tam;
 }
 
 /**
  * Libera a memória alocada para um show
  * @param s Ponteiro para o show a ser liberado
  */
 void liberar_show(Show *s) {
     if (s == NULL) return;
     
     // Libera strings individuais
     free(s->show_id);
     free(s->type);
     free(s->title);
     free(s->director);
     free(s->cast);
     free(s->country);
     free(s->date_added);
     free(s->release_year);
     free(s->rating);
     free(s->duration);
     free(s->listed_in);
     free(s->description);
     
     // Libera arrays de strings
     if (s->elenco != NULL) {
         for (int i = 0; i < s->tam_elenco; i++) {
             free(s->elenco[i]);
         }
         free(s->elenco);
     }
     
     if (s->listados != NULL) {
         for (int i = 0; i < s->tam_listados; i++) {
             free(s->listados[i]);
         }
         free(s->listados);
     }
 }
 
 /**
  * Remove espaços em branco no início e fim da string
  * @param str String a ser processada
  */
 void trim(char *str) {
     int start = 0;
     while (str[start] == ' ') start++;
     
     int end = strlen(str) - 1;
     while (end > start && str[end] == ' ') end--;
     
     int i, j = 0;
     for (i = start; i <= end; i++) {
         str[j++] = str[i];
     }
     str[j] = '\0';
 }
 
 /**
  * Extrai um campo de uma string CSV
  * @param next Ponteiro para a posição atual da string
  * @param extraido Ponteiro para armazenar o número de caracteres processados
  * @return String contendo o campo extraído
  */
 char *extraiCampo(char *next, int *extraido) {
     char *campo = malloc(1000);
     int i=0, j=0, verifica = 1, aspas = 0, caracteres = 0;
     
     // Processa a string até encontrar uma vírgula não cercada por aspas
     while(verifica && next[i] != '\0') {
         if(!aspas) {
             if(next[i] == ',') {
                 verifica = !verifica;
             } else {
                 if(next[i] == '"') {
                     aspas = !aspas;
                 } else {
                     campo[j++] = next[i];
                     caracteres++;
                 }
             }
         } else {
             if(next[i] == '"') {
                 aspas = !aspas;
             } else {
                 campo[j++] = next[i];
                 caracteres++;
             }
         }
         i++;
     }
     
     // Se o campo estiver vazio, substitui por "NaN"
     if(caracteres == 0) {
         campo[j++] = 'N';
         campo[j++] = 'a';
         campo[j++] = 'N';
     }
     
     campo[j] = '\0';
     *extraido = i;
     return campo;
 }
 
 /**
  * Separa uma string em um array de strings usando vírgula como delimitador
  * @param linha String a ser separada
  * @param tam Ponteiro para armazenar o tamanho do array resultante
  * @return Array de strings separadas e ordenadas
  */
 char **separaString(char *linha, int *tam) {
     int i = 0, j = 0, k = 0;
     
     // Conta o número de itens
     *tam = 1;
     for (i = 0; linha[i] != '\0'; i++) {
         if (linha[i] == ',') {
             (*tam)++;
         }
     }
     
     // Aloca o array de strings
     char **s = (char **)malloc(sizeof(char*) * (*tam));
     for (i = 0; i < *tam; i++) {
         s[i] = NULL;
     }
     
     // Separa a string
     i = j = k = 0;
     s[k] = (char *)malloc(sizeof(char) * (strlen(linha) + 1));
     while (linha[i] != '\0') {
         if (linha[i] == ',') {
             s[k][j] = '\0';
             trim(s[k]);
             k++;
             j = 0;
             s[k] = (char *)malloc(sizeof(char) * (strlen(linha) + 1));
         } else if (linha[i] != '"') {
             s[k][j++] = linha[i];
         }
         i++;
     }
     s[k][j] = '\0';
     trim(s[k]);
     
     // Ordena o array de strings
     for (int m = 0; m < *tam - 1; m++) {
         int menor = m;
         for (int n = m + 1; n < *tam; n++) {
             if (strcmp(s[n], s[menor]) < 0) {
                 menor = n;
             }
         }
         if (menor != m) {
             char *temp = s[m];
             s[m] = s[menor];
             s[menor] = temp;
         }
     }
     
     return s;
 }
 
 /**
  * Lê os dados de um show de uma linha CSV
  * @param linha Linha contendo os dados do show
  * @return Ponteiro para o show criado
  */
 Show *ler(char *linha) {
     Show *show = (Show *)malloc(sizeof(Show));
     int i = 0, offset = 0;
     char *campos[12];
     
     // Extrai os 12 campos do CSV
     for (int c = 0; c < 12; c++) {
         campos[c] = extraiCampo(linha + i, &offset);
         i += offset;
     }
     
     // Atribui os campos ao show
     show->show_id = campos[0];
     show->type = campos[1];
     show->title = campos[2];
     show->director = campos[3];
     show->cast = campos[4];
     show->country = campos[5];
     show->date_added = campos[6];
     show->release_year = campos[7];
     show->rating = campos[8];
     show->duration = campos[9];
     show->listed_in = campos[10];
     show->description = campos[11];
     
     // Processa elenco e categorias
     show->elenco = separaString(show->cast, &show->tam_elenco);
     show->listados = separaString(show->listed_in, &show->tam_listados);
     
     return show;
 }
 
 /**
  * Imprime um array de strings
  * @param array Array a ser impresso
  * @param tam Tamanho do array
  */
 void imprimiArray(char **array, int tam) {
     printf("[");
     for(int i=0; i<tam; i++) {
         printf("%s", array[i]);
         if(i < tam-1) {
             printf(", ");
         }
     }
     printf("]");
 }
 
 /**
  * Imprime os dados de um show
  * @param show Ponteiro para o show a ser impresso
  */
 void imprimir(Show *show) {
     printf("=> %s ## %s ## %s ## %s ## ", show->show_id, show->title, show->type, show->director);
     imprimiArray(show->elenco, show->tam_elenco);
     printf(" ## %s ## %s ## %s ## %s ## %s ## ", show->country, show->date_added, 
         show->release_year, show->rating, show->duration);
     imprimiArray(show->listados, show->tam_listados);
     printf(" ## \n");
 }
 
 /**
  * Compara duas strings e conta comparações
  * @param a Primeira string
  * @param b Segunda string
  * @param comparacoes Ponteiro para contador de comparações
  * @return Diferença entre as strings (negativo se a < b, 0 se iguais, positivo se a > b)
  */
 int comparaStrings(char *a, char *b, int *comparacoes) {
     (*comparacoes)++;
     int i = 0;
     while (a[i] && b[i]) {
         if (a[i] != b[i])
             return a[i] - b[i];
         i++;
     }
     return a[i] - b[i];
 }
 
 /**
  * Troca dois shows de posição e conta movimentações
  * @param a Ponteiro para o primeiro show
  * @param b Ponteiro para o segundo show
  * @param movimentacoes Ponteiro para contador de movimentações
  */
 void troca(Show **a, Show **b, int *movimentacoes) {
     Show *temp = *a;
     *a = *b;
     *b = temp;
     (*movimentacoes)++;
 }
 
 /**
  * Implementação recursiva do algoritmo de ordenação por seleção
  * @param shows Array de ponteiros para shows
  * @param n Tamanho do array
  * @param indice Índice atual da recursão
  * @param comparacoes Ponteiro para contador de comparações
  * @param movimentacoes Ponteiro para contador de movimentações
  */
 void selecaoRecursiva(Show **shows, int n, int indice, int *comparacoes, int *movimentacoes) {
     if (indice >= n - 1) return;
     
     // Encontra o menor elemento a partir do índice atual
     int min_index = indice;
     for (int i = indice + 1; i < n; i++) {
         if (comparaStrings(shows[i]->title, shows[min_index]->title, comparacoes) < 0) {
             min_index = i;
         }
     }
     
     // Troca o menor elemento com o atual se necessário
     if (min_index != indice) {
         troca(&shows[indice], &shows[min_index], movimentacoes);
     }
     
     // Chamada recursiva para o próximo índice
     selecaoRecursiva(shows, n, indice + 1, comparacoes, movimentacoes);
 }
 
 /**
  * Função principal
  */
 int main() {
     // Abre o arquivo CSV
     FILE *file = fopen("/tmp/disneyplus.csv", "r");
     if (file == NULL) {
         printf("Erro ao abrir o arquivo.\n");
         return 1;
     }
     
     // Lê todos os shows do arquivo
     char linha[TAM_LINHA];
     int i = 0;
     Show *shows[MAX_SHOWS];
     fgets(linha, TAM_LINHA, file);  // Descarta a linha de cabeçalho
     
     while (fgets(linha, TAM_LINHA, file) != NULL && i < MAX_SHOWS) {
         // Remove quebras de linha
         int j = 0;
         while (linha[j] != '\0') {
             if (linha[j] == '\n' || linha[j] == '\r') {
                 linha[j] = '\0';
             }
             j++;
         }
         
         shows[i] = ler(linha);
         i++;
     }
     fclose(file);
     
     // Lê os IDs dos shows a serem processados da entrada padrão
     Show *meus_shows[300];
     int k = 0;
     char s[50];
     
     while (scanf("%s", s) && strcmp(s, "FIM") != 0) {
         for (int j = 0; j < i; j++) {
             if (strcmp(s, shows[j]->show_id) == 0) {
                 meus_shows[k++] = shows[j];
                 j = i;  // Sai do loop após encontrar o show
             }
         }
     }
     
     // Ordena os shows selecionados
     int comparacoes = 0, movimentacoes = 0;
     clock_t inicio = clock();
     selecaoRecursiva(meus_shows, k, 0, &comparacoes, &movimentacoes);
     clock_t fim = clock();
     
     // Imprime os shows ordenados
     for (int j = 0; j < k; j++) {
         imprimir(meus_shows[j]);
     }
     
     // Gera arquivo de log
     double tempo = ((double)(fim - inicio)) / CLOCKS_PER_SEC;
     FILE *log = fopen("1404192.txt", "w");
     fprintf(log, "matricula: 1404192\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %lf\n", 
             comparacoes, movimentacoes, tempo);
     fclose(log);
     
     // Libera a memória alocada
     for(int j = 0; j < i; j++) {
         liberar_show(shows[j]);
     }
     
     return 0;
 }
/**
 * Implementação de Shellsort para ordenação de shows do Disney+
 * @author Lucas Nascimento
 * Matrícula: 1404192
 */

 #include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 #include <time.h>
 
 // Constantes
 #define MAX_SHOWS 1368
 #define TAM_LINHA 1000
 #define MAX_FIELD_SIZE 500
 
 /**
  * Estrutura para armazenar informações de um show
  */
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
     char **elenco;
     char **listados;
     int tam_elenco;
     int tam_listados;
     int dia, mes, ano;
     int tem_data;
 } Show;
 
 /**
  * Funções de utilidade para manipulação de strings e memória
  */
 
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
     char *campo = malloc(MAX_FIELD_SIZE);
     int i = 0, j = 0, verifica = 1, aspas = 0, caracteres = 0;
     
     // Processa a string até encontrar uma vírgula não cercada por aspas
     while(verifica && next[i] != '\0') {
         if(!aspas) {
             if(next[i] == ',') {
                 verifica = 0;
             } else if(next[i] == '"') {
                 aspas = 1;
             } else {
                 campo[j++] = next[i];
                 caracteres++;
             }
         } else {
             if(next[i] == '"') {
                 aspas = 0;
             } else {
                 campo[j++] = next[i];
                 caracteres++;
             }
         }
         i++;
     }
     
     // Se o campo estiver vazio, substitui por "NaN"
     if(caracteres == 0) {
         strcpy(campo, "NaN");
         j = 3;
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
     if (strcmp(linha, "NaN") == 0) {
         *tam = 0;
         return NULL;
     }
     
     // Conta o número de itens
     int i = 0, j = 0, k = 0;
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
     s[k] = (char *)malloc(strlen(linha) + 1);
     while (linha[i] != '\0') {
         if (linha[i] == ',') {
             s[k][j] = '\0';
             trim(s[k]);
             k++;
             j = 0;
             s[k] = (char *)malloc(strlen(linha) + 1);
         } else if (linha[i] != '"') {
             s[k][j++] = linha[i];
         }
         i++;
     }
     s[k][j] = '\0';
     trim(s[k]);
     
     // Ordena o array de strings usando algoritmo de ordenação de seleção
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
  * Libera a memória alocada para um show
  * @param s Ponteiro para o show a ser liberado
  */
 void liberar_show(Show *s) {
     if (s == NULL) return;
     
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
     
     free(s);
 }
 
 /**
  * Lê os dados de um show de uma linha CSV
  * @param linha Linha contendo os dados do show
  * @return Ponteiro para o show criado
  */
 Show *ler(char *linha) {
     Show *show = (Show *)malloc(sizeof(Show));
     int i = 0, offset = 0;
     
     // Extrai os 12 campos do CSV
     show->show_id = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->type = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->title = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->director = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->cast = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->country = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->date_added = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->release_year = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->rating = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->duration = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->listed_in = extraiCampo(linha + i, &offset);
     i += offset;
     
     show->description = extraiCampo(linha + i, &offset);
     
     // Processa elenco e categorias
     show->elenco = separaString(show->cast, &show->tam_elenco);
     show->listados = separaString(show->listed_in, &show->tam_listados);
     
     // Inicializa campos de data
     show->dia = 0;
     show->mes = 0;
     show->ano = 0;
     show->tem_data = 0;
     
     return show;
 }
 
 /**
  * Imprime um array de strings
  * @param array Array a ser impresso
  * @param tam Tamanho do array
  */
 void imprimiArray(char **array, int tam) {
     printf("[");
     for(int i = 0; i < tam; i++) {
         printf("%s", array[i]);
         if(i < tam - 1) {
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
  * Compara dois shows por type e title (desempate)
  * @param a Primeiro show
  * @param b Segundo show
  * @param comparacoes Ponteiro para contador de comparações
  * @return Valor negativo se a < b, 0 se iguais, positivo se a > b
  */
 int compararShows(Show *a, Show *b, int *comparacoes) {
     (*comparacoes)++;
     int cmp = strcmp(a->type, b->type);
     
     // Em caso de empate, compara por title
     if (cmp == 0) {
         (*comparacoes)++;
         cmp = strcmp(a->title, b->title);
     }
     
     return cmp;
 }
 
 /**
  * Implementação do algoritmo Shellsort
  * @param shows Array de ponteiros para shows
  * @param n Tamanho do array
  * @param comparacoes Ponteiro para contador de comparações
  * @param movimentacoes Ponteiro para contador de movimentações
  */
 void shellsort(Show **shows, int n, int *comparacoes, int *movimentacoes) {
     int h = 1;
     
     // Calcula o valor inicial de h
     do { 
         h = h * 3 + 1; 
     } while (h < n);
     
     // Processo de ordenação
     do {
         h /= 3;
         
         for (int i = h; i < n; i++) {
             Show *temp = shows[i];
             int j = i - h;
             
             while (j >= 0 && compararShows(shows[j], temp, comparacoes) > 0) {
                 shows[j + h] = shows[j];
                 (*movimentacoes)++;
                 j -= h;
             }
             
             shows[j + h] = temp;
             (*movimentacoes)++;
         }
     } while (h != 1);
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
     int totalShows = 0;
     Show *shows[MAX_SHOWS];
     
     // Pula a linha de cabeçalho
     fgets(linha, TAM_LINHA, file);
     
     while (fgets(linha, TAM_LINHA, file) != NULL && totalShows < MAX_SHOWS) {
         // Remove quebras de linha
         int j = 0;
         while (linha[j] != '\0') {
             if (linha[j] == '\n' || linha[j] == '\r') {
                 linha[j] = '\0';
             }
             j++;
         }
         
         shows[totalShows] = ler(linha);
         totalShows++;
     }
     fclose(file);
     
     // Lê os IDs dos shows a serem processados da entrada padrão
     Show *meusShows[MAX_SHOWS];
     int numMeusShows = 0;
     char id[50];
     
     while (scanf("%s", id) == 1) {
         if (strcmp(id, "FIM") == 0) {
             break;
         }
         
         for (int j = 0; j < totalShows; j++) {
             if (strcmp(id, shows[j]->show_id) == 0) {
                 meusShows[numMeusShows++] = shows[j];
                 break;
             }
         }
     }
     
     // Ordena os shows usando Shellsort
     int comparacoes = 0, movimentacoes = 0;
     clock_t inicio = clock();
     shellsort(meusShows, numMeusShows, &comparacoes, &movimentacoes);
     clock_t fim = clock();
     
     // Imprime os shows ordenados
     for (int j = 0; j < numMeusShows; j++) {
         imprimir(meusShows[j]);
     }
     
     // Gera arquivo de log
     double tempo = ((double)(fim - inicio)) / CLOCKS_PER_SEC;
     FILE *log = fopen("1404192_shellsort.txt", "w");
     fprintf(log, "matricula: 1404192\tcomparacoes: %d\tmovimentacoes: %d\ttempo execucao: %lf s\n", 
             comparacoes, movimentacoes, tempo);
     fclose(log);
     
     // Libera a memória alocada
     for (int j = 0; j < totalShows; j++) {
         liberar_show(shows[j]);
     }
     
     return 0;
 }
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#define MAX_SHOWS 1368
#define TAM_LINHA 1000

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

int getTam(char* linha){
    int tam = 0;
    while(linha[tam] != '\0'){
        tam++;
    }
    return tam;
}

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
}

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

char *extraiCampo(char *next, int *extraido){
    char *campo = malloc(1000);
    int i=0, j=0, verifica = 1, aspas = 0, caracteres = 0;

    while(verifica && next[i] != '\0' ){
        if(!aspas){
            if(next[i] == ','){
                verifica = !verifica;
            }else{
                if(next[i] == '"'){
                    aspas = !aspas;
                }else{
                campo[j++] = next[i];
                caracteres++;
                }
            }
        }else{
            if(next[i] == '"'){
                aspas = !aspas;
            }else{
            campo[j++] = next[i];
            caracteres++;
            }
        }
        i++;
    }
    if(caracteres == 0){
        campo[j++] = 'N';
        campo[j++] = 'a';
        campo[j++] = 'N';
    }
    campo[j] = '\0';
    *extraido = i;

    return campo;
}

char **separaString(char *linha, int *tam) {
    int i = 0, j = 0, k = 0;
    *tam = 1;

    for (i = 0; linha[i] != '\0'; i++) {
        if (linha[i] == ',') {
            (*tam)++;
        }
    }

    char **s = (char **)malloc(sizeof(char*) * (*tam));
    for (i = 0; i < *tam; i++) {
        s[i] = NULL;
    }

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

Show *ler(char *linha){
    Show *show = (Show *)malloc(sizeof(Show));
    int i = 0, offset = 0;
    char *campos[12];

    for (int c = 0; c < 12; c++) {
        campos[c] = extraiCampo(linha + i, &offset);
        i += offset;
    }

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

    show->elenco = separaString(show->cast, &show->tam_elenco);
    show->listados = separaString(show->listed_in, &show->tam_listados);

    return show;
}

void imprimiArray(char **array, int tam){
    printf("[");
    for(int i=0; i<tam; i++){
        printf("%s", array[i]);
        if(i < tam-1){
            printf(", ");
        }
    }
    printf("]");
}

void imprimir(Show *show){
    printf("=> %s ## %s ## %s ## %s ## ", show->show_id, show->title, show->type, show->director);
    imprimiArray(show->elenco, show->tam_elenco);
    printf(" ## %s ## %s ## %s ## %s ## %s ## ", show->country, show->date_added, 
        show->release_year, show->rating, show->duration);
    imprimiArray(show->listados, show->tam_listados);
    printf(" ## \n");
}

int comparar_shows(const Show *a, const Show *b, int *comparacoes) {
    (*comparacoes)++;
    // Comparação primária pelo director
    int cmp_director = strcmp(a->director, b->director);
    if (cmp_director != 0) {
        return cmp_director;
    }
    // Desempate pelo title
    return strcmp(a->title, b->title);
}

void heapify(Show *arr[], int n, int i, int *comparacoes, int *movimentacoes) {
    int maior = i;
    int esquerda = 2 * i + 1;
    int direita = 2 * i + 2;

    // Comparação com o filho esquerdo
    if (esquerda < n && comparar_shows(arr[esquerda], arr[maior], comparacoes) > 0) {
        maior = esquerda;
    }

    // Comparação com o filho direito
    if (direita < n && comparar_shows(arr[direita], arr[maior], comparacoes) > 0) {
        maior = direita;
    }

    // Se o maior não for o índice inicial, fazemos a troca
    if (maior != i) {
        Show *temp = arr[i];
        arr[i] = arr[maior];
        arr[maior] = temp;
        (*movimentacoes) += 3; // 3 movimentações: 2 para os ponteiros e 1 para o temp

        heapify(arr, n, maior, comparacoes, movimentacoes);
    }
}

void heapsort(Show *arr[], int n, int *comparacoes, int *movimentacoes) {
    // Construção do heap máximo
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(arr, n, i, comparacoes, movimentacoes);
    }

    // Ordenação do heap
    for (int i = n - 1; i >= 1; i--) {
        Show *temp = arr[0];
        arr[0] = arr[i];
        arr[i] = temp;
        (*movimentacoes) += 3; // 3 movimentações para a troca

        heapify(arr, i, 0, comparacoes, movimentacoes);
    }
}

int main() {
    FILE *file = fopen("/tmp/disneyplus.csv", "r");
    if (file == NULL) {
        printf("Erro ao abrir o arquivo.\n");
        return 1;
    }

    char linha[TAM_LINHA];
    int i = 0;

    Show *shows[MAX_SHOWS];

    fgets(linha, TAM_LINHA, file);

    while (fgets(linha, TAM_LINHA, file) != NULL && i < MAX_SHOWS) {
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

    Show *meus_shows[300];
    int k = 0;
    char s[50];

    while (scanf("%s", s) && strcmp(s, "FIM") != 0) {
        for (int j = 0; j < i; j++) {
            if (strcmp(s, shows[j]->show_id) == 0) {
                meus_shows[k++] = shows[j];
                j = i;
            }
        }
    }

    int comparacoes = 0, movimentacoes = 0;
    clock_t inicio = clock();
    heapsort(meus_shows, k, &comparacoes, &movimentacoes);
    clock_t fim = clock();

    for (int j = 0; j < 10; j++) {
        imprimir(meus_shows[j]);
    }

    double tempo = ((double)(fim - inicio)) / CLOCKS_PER_SEC;
    FILE *log = fopen("matricula_heapsort.txt", "w");
    fprintf(log, "matricula: 1404192\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %lf\n", comparacoes, movimentacoes, tempo);
    fclose(log);

    for(int j = 0; j < i; j++) {
        liberar_show(shows[j]);
    }

    return 0;
}
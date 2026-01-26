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
    
    int ano_a = atoi(a->release_year);
    int ano_b = atoi(b->release_year);

    if (ano_a != ano_b) {
        return ano_a - ano_b;
    }

    // Se os anos forem iguais, ordenar por título
    return strcmp(a->title, b->title);
}

int obter_digito(int numero, int digito) {
    return (numero / digito) % 10;
}

void radixsort(Show *arr[], int n, int *comparacoes, int *movimentacoes) {
    int maior = 0;
    for (int i = 0; i < n; i++) {
        int ano = atoi(arr[i]->release_year);
        if (ano > maior) {
            maior = ano;
        }
    }

    // Ordenação por dígito do ano
    for (int exp = 1; maior / exp > 0; exp *= 10) {
        Show *output[n];
        int contagem[10] = {0};

        // Contagem de ocorrências de cada dígito
        for (int i = 0; i < n; i++) {
            int digito = obter_digito(atoi(arr[i]->release_year), exp);
            contagem[digito]++;
        }

        // Acumulando a contagem para estabilizar
        for (int i = 1; i < 10; i++) {
            contagem[i] += contagem[i - 1];
        }

        // Organizar os elementos de acordo com o dígito atual
        for (int i = n - 1; i >= 0; i--) {
            int digito = obter_digito(atoi(arr[i]->release_year), exp);
            output[contagem[digito] - 1] = arr[i];
            contagem[digito]--;
            (*movimentacoes)++;
        }

        // Atualizando o arr com a ordenação baseada no dígito atual
        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }
    }

    // Ordenação adicional pelo título, para desempatar
    for (int i = 0; i < n - 1; i++) {
        for (int j = i + 1; j < n; j++) {
            // Comparar apenas os títulos, já que o ano já está ordenado
            if (atoi(arr[i]->release_year) == atoi(arr[j]->release_year) && strcmp(arr[i]->title, arr[j]->title) > 0) {
                Show *temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                (*movimentacoes)++;
            }
            (*comparacoes)++;
        }
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
    radixsort(meus_shows, k, &comparacoes, &movimentacoes);
    clock_t fim = clock();

    for (int j = 0; j < k; j++) {
        imprimir(meus_shows[j]);
    }

    double tempo = ((double)(fim - inicio)) / CLOCKS_PER_SEC;
    FILE *log = fopen("matricula_radixsort.txt", "w");
    fprintf(log, "matricula: 1404192\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %lf\n", comparacoes, movimentacoes, tempo);
    fprintf(log, "O Radix Sort é um algoritmo de ordenação que, em vez de comparar elementos diretamente,\n"
        "ordena os dados com base nas suas partes, como os dígitos de números ou caracteres de strings.\n"
        "Ele começa ordenando pelos dígitos menos significativos e vai avançando até o dígito mais significativo.\n\n"
        
        "A principal vantagem do Radix Sort é que ele não precisa fazer comparações diretas entre os elementos,\n"
        "o que pode torná-lo mais eficiente em certos casos. Ele usa um algoritmo de ordenação estável, como o\n"
        "Counting Sort, para ordenar os elementos em cada 'passagem' (por dígito ou caractere).\n\n"
        
        "O Radix Sort tem complexidade O(d * n), onde n é o número de elementos e d é o número de dígitos ou\n"
        "caracteres. Ele é eficiente quando d é pequeno, mas pode ser mais lento que algoritmos como QuickSort e\n"
        "MergeSort quando d é grande.");
    fclose(log);

    for(int j = 0; j < i; j++) {
        liberar_show(shows[j]);
    }

    return 0;
}
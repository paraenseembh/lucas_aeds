#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX_SHOWS 1368
#define TAM_LINHA 1000

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
} Show;

/**
 * Obtém o tamanho de uma string
 */
int getTam(char* linha) {
    int tam = 0;
    while (linha[tam] != '\0') {
        tam++;
    }
    return tam;
}

/**
 * Remove espaços em branco do início e do fim da string
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
 * Extrai um campo da linha CSV, considerando aspas e vírgulas
 */
char *extraiCampo(char *next, int *extraido) {
    char *campo = malloc(1000);
    int i = 0, j = 0, verifica = 1, aspas = 0, caracteres = 0;

    while (verifica && next[i] != '\0') {
        if (!aspas) {
            if (next[i] == ',') {
                verifica = !verifica;
            } else {
                if (next[i] == '"') {
                    aspas = !aspas;
                } else {
                    campo[j++] = next[i];
                    caracteres++;
                }
            }
        } else {
            if (next[i] == '"') {
                aspas = !aspas;
            } else {
                campo[j++] = next[i];
                caracteres++;
            }
        }
        i++;
    }
    
    // Se o campo estiver vazio, preencha com "NaN"
    if (caracteres == 0) {
        campo[j++] = 'N';
        campo[j++] = 'a';
        campo[j++] = 'N';
    }
    
    campo[j] = '\0';
    *extraido = i;

    return campo;
}

/**
 * Separa uma string por vírgulas e ordena os itens
 */
char **separaString(char *linha, int *tam) {
    int i = 0, j = 0, k = 0;
    *tam = 1;

    // Conta o número de itens separados por vírgula
    for (i = 0; linha[i] != '\0'; i++) {
        if (linha[i] == ',') {
            (*tam)++;
        }
    }

    // Aloca memória para o array de strings
    char **s = (char **)malloc(sizeof(char*) * (*tam));
    for (i = 0; i < *tam; i++) {
        s[i] = NULL;
    }

    // Separa a string por vírgulas
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
 * Lê uma linha do arquivo CSV e cria um objeto Show
 */
Show *ler(char *linha) {
    Show *show = (Show *)malloc(sizeof(Show));
    int i = 0, offset = 0;
    char *campos[12];

    // Extrai todos os campos da linha
    for (int c = 0; c < 12; c++) {
        campos[c] = extraiCampo(linha + i, &offset);
        i += offset;
    }

    // Atribui os campos ao objeto Show
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

    // Processa os arrays
    show->elenco = separaString(show->cast, &show->tam_elenco);
    show->listados = separaString(show->listed_in, &show->tam_listados);

    return show;
}

/**
 * Imprime um array de strings no formato [item1, item2, ...]
 */
void imprimiArray(char **array, int tam) {
    printf("[");
    for (int i = 0; i < tam; i++) {
        printf("%s", array[i]);
        if (i < tam - 1) {
            printf(", ");
        }
    }
    printf("]");
}

/**
 * Imprime os dados de um show no formato especificado
 */
void imprimir(Show *show) {
    printf("=> %s ## %s ## %s ## %s ## ", 
           show->show_id, show->title, show->type, show->director);
    
    imprimiArray(show->elenco, show->tam_elenco);
    
    printf(" ## %s ## %s ## %s ## %s ## %s ## ", 
           show->country, show->date_added, show->release_year, 
           show->rating, show->duration);
    
    imprimiArray(show->listados, show->tam_listados);
    
    printf(" ## \n");
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

    char linha[TAM_LINHA];
    int i = 0;
    Show *shows[MAX_SHOWS];

    // Pula a linha de cabeçalho
    fgets(linha, TAM_LINHA, file);

    // Lê todos os shows do CSV
    while (fgets(linha, TAM_LINHA, file) != NULL && i < MAX_SHOWS) {
        // Remove caracteres de nova linha
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

    // Processa as entradas do usuário
    char entrada[20];
    scanf("%s", entrada);
    while (strcmp(entrada, "FIM") != 0) {
        for (int j = 0; j < i; j++) {
            if (strcmp(entrada, shows[j]->show_id) == 0) {
                imprimir(shows[j]);
                j = i;  // Força a saída do loop
            }
        }
        scanf("%s", entrada);
    }

    // Libera a memória alocada
    for (int j = 0; j < i; j++) {
        // Libera os elementos do array elenco
        for (int k = 0; k < shows[j]->tam_elenco; k++) {
            free(shows[j]->elenco[k]);
        }
        free(shows[j]->elenco);

        // Libera os elementos do array listados
        for (int k = 0; k < shows[j]->tam_listados; k++) {
            free(shows[j]->listados[k]);
        }
        free(shows[j]->listados);

        // Libera os campos de string
        free(shows[j]->show_id);
        free(shows[j]->type);
        free(shows[j]->title);
        free(shows[j]->director);
        free(shows[j]->cast);
        free(shows[j]->country);
        free(shows[j]->date_added);
        free(shows[j]->release_year);
        free(shows[j]->rating);
        free(shows[j]->duration);
        free(shows[j]->listed_in);
        free(shows[j]->description);

        // Libera a estrutura Show
        free(shows[j]);
    }

    return 0;
}
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

typedef struct {
    char show_id[20];
    char title[200];
    char* cast;
    char* listed_in;
} Show;

// Protótipos
void readCsv(Show shows[], int* count);
bool binarySearch(Show shows[], int count, char* title, int* comparisons);
void sortByTitle(Show shows[], int count);
void clearBuffer();

int main() {
    Show shows[1368];
    int count = 0;
    
    // Lê o arquivo CSV
    readCsv(shows, &count);
    
    // Ordena os shows pelo título
    sortByTitle(shows, count);
    
    // Lê os IDs e seleciona os shows
    Show selectedShows[1368];
    int selectedCount = 0;
    char id[20];
    
    scanf("%s", id);
    while (strcmp(id, "FIM") != 0) {
        for (int i = 0; i < count; i++) {
            if (strcmp(shows[i].show_id, id) == 0) {
                selectedShows[selectedCount++] = shows[i];
                break;
            }
        }
        scanf("%s", id);
    }
    
    clearBuffer();
    sortByTitle(selectedShows, selectedCount);
    
    // Pesquisa binária
    int comparisons = 0;
    clock_t start = clock();
    
    char title[200];
    scanf(" %[^\n]", title);
    
    while (strcmp(title, "FIM") != 0) {
        bool found = binarySearch(selectedShows, selectedCount, title, &comparisons);
        printf("%s\n", found ? "SIM" : "NAO");
        scanf(" %[^\n]", title);
    }
    
    // Gera arquivo de log
    clock_t end = clock();
    double time_spent = (double)(end - start) / CLOCKS_PER_SEC;
    
    FILE* log_file = fopen("1404192_binaria.txt", "w");
    fprintf(log_file, "1404192\t%.2f\t%d", time_spent, comparisons);
    fclose(log_file);
    
    // Libera memória
    for (int i = 0; i < count; i++) {
        free(shows[i].cast);
        free(shows[i].listed_in);
    }
    
    return 0;
}

void readCsv(Show shows[], int* count) {
    FILE* file = fopen("disneyplus.csv", "r");
    
    if (!file) {
        printf("Erro ao abrir arquivo.\n");
        exit(1);
    }
    
    char line[10000];
    fgets(line, sizeof(line), file); // Pula cabeçalho
    
    *count = 0;
    while (fgets(line, sizeof(line), file) && *count < 1368) {
        char* token;
        char* rest = line;
        int field = 0;
        
        while ((token = strtok_r(rest, ",", &rest)) && field < 11) {
            // Trata campos com vírgulas (entre aspas)
            if (token[0] == '"') {
                char buffer[10000] = {0};
                strcpy(buffer, token + 1);
                
                while (buffer[strlen(buffer) - 1] != '"' && (token = strtok_r(NULL, ",", &rest))) {
                    strcat(buffer, ",");
                    strcat(buffer, token);
                }
                
                if (strlen(buffer) > 0 && buffer[strlen(buffer) - 1] == '"')
                    buffer[strlen(buffer) - 1] = '\0';
                
                token = buffer;
            }
            
            // Atribui os campos necessários
            switch (field) {
                case 0: strcpy(shows[*count].show_id, token); break;
                case 2: strcpy(shows[*count].title, token); break;
                case 4: shows[*count].cast = strdup(token); break;
                case 10: shows[*count].listed_in = strdup(token); break;
            }
            field++;
        }
        (*count)++;
    }
    
    fclose(file);
}

void clearBuffer() {
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
}

int compareTitle(const void* a, const void* b) {
    return strcmp(((Show*)a)->title, ((Show*)b)->title);
}

void sortByTitle(Show shows[], int count) {
    qsort(shows, count, sizeof(Show), compareTitle);
}

bool binarySearch(Show shows[], int count, char* title, int* comparisons) {
    int left = 0, right = count - 1;
    
    while (left <= right) {
        int middle = (left + right) / 2;
        (*comparisons)++;
        
        int comparison = strcmp(shows[middle].title, title);
        
        if (comparison == 0) return true;
        else if (comparison < 0) left = middle + 1;
        else right = middle - 1;
    }
    
    return false;
}
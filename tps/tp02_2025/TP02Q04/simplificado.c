#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int main() {
    FILE* log_file = fopen("teste_log.txt", "w");
    if (log_file == NULL) {
        perror("Erro ao criar arquivo de log");
        return 1;
    }

    fprintf(log_file, "Teste de log\n");
    fclose(log_file);

    printf("Teste concluído. Verifique se o arquivo teste_log.txt foi criado.\n");
    return 0;
}
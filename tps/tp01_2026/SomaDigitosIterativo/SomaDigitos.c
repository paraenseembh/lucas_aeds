#include <stdio.h>
#include <string.h>

int somaDigitos(char *s) {
    int soma = 0;
    for (int i = 0; s[i] != '\0'; i++) {
        if (s[i] >= '0' && s[i] <= '9')
            soma += s[i] - '0';
    }
    return soma;
}

int main() {
    char linha[10000];
    while (fgets(linha, sizeof(linha), stdin)) {
        int len = strlen(linha);
        if (len > 0 && linha[len-1] == '\n') linha[--len] = '\0';
        if (strcmp(linha, "FIM") == 0) break;
        printf("%d\n", somaDigitos(linha));
    }
    return 0;
}

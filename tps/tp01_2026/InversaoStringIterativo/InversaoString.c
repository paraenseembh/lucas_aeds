#include <stdio.h>
#include <string.h>

// Retorna o comprimento em bytes do caractere UTF-8 que começa em c
int utf8Len(unsigned char c) {
    if (c < 0x80) return 1;
    if (c < 0xE0) return 2;
    if (c < 0xF0) return 3;
    return 4;
}

void inverter(char *s) {
    int n = strlen(s);
    // Coleta índices de início de cada caractere UTF-8
    int starts[10000];
    int nchars = 0;
    for (int i = 0; i < n; ) {
        starts[nchars++] = i;
        i += utf8Len((unsigned char)s[i]);
    }
    starts[nchars] = n;

    // Monta string invertida
    char resultado[10000];
    int pos = 0;
    for (int i = nchars - 1; i >= 0; i--) {
        int len = starts[i + 1] - starts[i];
        for (int j = 0; j < len; j++) {
            resultado[pos++] = s[starts[i] + j];
        }
    }
    resultado[pos] = '\0';
    strcpy(s, resultado);
}

int main() {
    char linha[10000];
    while (fgets(linha, sizeof(linha), stdin)) {
        int len = strlen(linha);
        if (len > 0 && linha[len - 1] == '\n') linha[--len] = '\0';
        if (strcmp(linha, "FIM") == 0) break;
        inverter(linha);
        printf("%s\n", linha);
    }
    return 0;
}

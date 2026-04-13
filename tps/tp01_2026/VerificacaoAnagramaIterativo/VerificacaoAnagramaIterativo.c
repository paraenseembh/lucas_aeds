#include <stdio.h>
#include <string.h>

int saoAnagramas(char *s1, char *s2) {
    int n1 = strlen(s1), n2 = strlen(s2);
    if (n1 != n2) return 0;
    int contador[256] = {0};
    for (int i = 0; i < n1; i++) {
        unsigned char c = s1[i];
        if (c >= 'A' && c <= 'Z') c += 32;
        contador[c]++;
    }
    for (int i = 0; i < n2; i++) {
        unsigned char c = s2[i];
        if (c >= 'A' && c <= 'Z') c += 32;
        contador[c]--;
    }
    for (int i = 0; i < 256; i++) {
        if (contador[i] != 0) return 0;
    }
    return 1;
}

int main() {
    char linha[10000];
    while (fgets(linha, sizeof(linha), stdin)) {
        int len = strlen(linha);
        if (len > 0 && linha[len - 1] == '\n') linha[--len] = '\0';
        if (strcmp(linha, "FIM") == 0) break;
        char s1[5000], s2[5000];
        if (sscanf(linha, "%s %s", s1, s2) == 2) {
            printf(saoAnagramas(s1, s2) ? "SIM\n" : "NAO\n");
        }
    }
    return 0;
}

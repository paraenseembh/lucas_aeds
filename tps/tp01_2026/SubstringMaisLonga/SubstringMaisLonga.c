#include <stdio.h>
#include <string.h>

int maiorSubstring(char *s) {
    int ultimo[256];
    for (int i = 0; i < 256; i++) ultimo[i] = -1;
    int max = 0, inicio = 0;
    int n = strlen(s);
    for (int i = 0; i < n; i++) {
        unsigned char c = s[i];
        if (ultimo[c] >= inicio) inicio = ultimo[c] + 1;
        ultimo[c] = i;
        if (i - inicio + 1 > max) max = i - inicio + 1;
    }
    return max;
}

int main() {
    char linha[10000];
    while (fgets(linha, sizeof(linha), stdin)) {
        int len = strlen(linha);
        if (len > 0 && linha[len - 1] == '\n') linha[--len] = '\0';
        if (strcmp(linha, "FIM") == 0) break;
        printf("%d\n", maiorSubstring(linha));
    }
    return 0;
}

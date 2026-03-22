#include <stdio.h>
#include <string.h>

void inverter(char *s) {
    int n = strlen(s);
    for (int i = 0; i < n / 2; i++) {
        char tmp = s[i];
        s[i] = s[n - 1 - i];
        s[n - 1 - i] = tmp;
    }
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

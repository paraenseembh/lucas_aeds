#include <stdio.h>
#include <string.h>
#include <stdbool.h>

void cifrarCesar(char texto[], int chave) {
    chave = (chave % 26 + 26) % 26;
    for (int i = 0; texto[i] != '\0'; i++) {
        char c = texto[i];
        if (c >= 'A' && c <= 'Z') {
            texto[i] = (c - 'A' + chave) % 26 + 'A';
        } else if (c >= 'a' && c <= 'z') {
            texto[i] = (c - 'a' + chave) % 26 + 'a';
        }
    }
}

bool EhFim(char s[]) {
    return (strlen(s) >= 3 && s[0] == 'F' && s[1] == 'I' && s[2] == 'M');
}

int main() {
    char entrada[1000];
    int chave = 3; // Exemplo de deslocamento

    while (fgets(entrada, sizeof(entrada), stdin)) {
        // Remove a quebra de linha do fgets
        entrada[strcspn(entrada, "\n")] = '\0';

        if (EhFim(entrada)) {
            break;
        }

        cifrarCesar(entrada, chave);
        printf("%s\n", entrada);
    }

    return 0;
}

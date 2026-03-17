#include <stdio.h>
#include <string.h>
#include <stdbool.h>

// Função recursiva que cifra apenas caracteres ASCII básicos
void cifrarRecursivo(char texto[], int i, int chave) {
    if (texto[i] == '\0') {
        return;
    }

    // A regra padrão para esse exercício geralmente é:
    // Cifrar apenas caracteres da tabela ASCII visível (espaços e letras)
    // Se o seu gabarito diz que "ê" continua "ê", então filtramos por valor ASCII:
    // Caracteres estendidos/acentuados em C costumam ser negativos ou > 126
    if (texto[i] >= 0 && texto[i] <= 126) {
        texto[i] = (char)(texto[i] + chave);
    }

    cifrarRecursivo(texto, i + 1, chave);
}

bool EhFim(char s[]) {
    // strcmp é mais seguro para verificar a palavra exata
    return (strcmp(s, "FIM") == 0);
}

int main() {
    char entrada[1000];
    int deslocamento = 3;

    // Lendo a linha inteira
    while (fgets(entrada, sizeof(entrada), stdin)) {
        // Remove o \n final
        entrada[strcspn(entrada, "\n\r")] = '\0';

        if (EhFim(entrada)) {
            break;
        }

        cifrarRecursivo(entrada, 0, deslocamento);
        
        printf("%s\n", entrada);
    }

    return 0;
}

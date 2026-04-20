#include <stdio.h>
#include <string.h>
#include <stdbool.h>

// Método não recursivo que verifica se a string de entrada é "FIM"
bool stop(char* input) {
    return strlen(input) == 3 && input[0] == 'F' && 
           input[1] == 'I' && input[2] == 'M';
}

// Método recursivo para verificar se é palíndromo
bool isPalRecursive(char* input, int i, int len) {
    bool resp;
    
    if (i >= len / 2) {
        resp = true;
    } else if (input[i] != input[len - 1 - i]) {
        resp = false;
    } else {
        resp = isPalRecursive(input, i + 1, len);
    }
    
    return resp;
}

// Método wrapper que inicializa a recursão para verificar se é palíndromo
bool isPal(char* input) {
    return isPalRecursive(input, 0, strlen(input));
}

// Método recursivo para processar as entradas
void processInputs() {
    char input[1000];
    
    // Lê a linha de entrada
    fgets(input, sizeof(input), stdin);
    
    // Remove o caractere de nova linha (\n) do final da string, se existir
    int len = strlen(input);
    if (len > 0 && input[len-1] == '\n') {
        input[len-1] = '\0';
    }
    
    // Verifica se é para parar
    if (!stop(input)) {
        // Verifica se é palíndromo e imprime o resultado
        if (isPal(input)) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }
        
        // Chama recursivamente para processar a próxima entrada
        processInputs();
    }
}

int main() {
    // Inicia o processamento recursivo das entradas
    processInputs();
    
    return 0;
}
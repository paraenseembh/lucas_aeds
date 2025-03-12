#include <stdio.h>
#include <stdbool.h>
#include <string.h>

bool stop(char* input)
{   
    bool resp = false;

    if(strlen(input) == 3 && input[0] == 'F' && input[1] == 'I' && input[2] == 'M')
    {
        resp = true;
    }

    return resp;
}

int somaDigitosRecursivo(int num) {
    return num < 10 ? num : num % 10 + somaDigitosRecursivo(num / 10);
}

int main() {
    char input[100];
    
    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = '\0'; // Remove a quebra de linha
    
    while(!stop(input)) {
        int num;
        sscanf(input, "%d", &num);
        
        int resultado = somaDigitosRecursivo(num);
        printf("%d\n", resultado);
        
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0'; // Remove a quebra de linha
    }
    
    return 0;
}
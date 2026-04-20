#include <stdio.h>
#include <string.h>
#include <stdbool.h>

bool stop(char *input) {
    bool resp = false;
    
    if (strlen(input) == 3 && input[0] == 'F' && input[1] == 'I' && input[2] == 'M') {
        resp = true;
    }
    
    return resp;
}

void inverteString(char *input, char *resultado) {
    int n = strlen(input);
    
    for (int i = 0; i < n; i++) {
        resultado[i] = input[n-1-i];
    }
    resultado[n] = '\0';
}

int main() {
    char input[1000];
    char resultado[1000];
    
    fgets(input, 1000, stdin);
    input[strcspn(input, "\n")] = '\0';
    
    while (!stop(input)) {
        inverteString(input, resultado);
        printf("%s\n", resultado);
        
        fgets(input, 1000, stdin);
        input[strcspn(input, "\n")] = '\0';
    }
    
    return 0;
}
#include <stdio.h>
#include <string.h>

int ehVogal(char c) {
    return c=='a'||c=='e'||c=='i'||c=='o'||c=='u'||
           c=='A'||c=='E'||c=='I'||c=='O'||c=='U';
}

int ehLetraASCII(char c) {
    return (c>='a'&&c<='z')||(c>='A'&&c<='Z');
}

int somenteVogais(char *s, int i) {
    if (s[i] == '\0') return 1;
    if (!ehLetraASCII(s[i]) || !ehVogal(s[i])) return 0;
    return somenteVogais(s, i + 1);
}

int somenteConsoantes(char *s, int i) {
    if (s[i] == '\0') return 1;
    if (!ehLetraASCII(s[i]) || ehVogal(s[i])) return 0;
    return somenteConsoantes(s, i + 1);
}

int ehInt(char *s, int i, int qtd) {
    if (s[i] == '\0') return qtd > 0;
    if (s[i] < '0' || s[i] > '9') return 0;
    return ehInt(s, i + 1, qtd + 1);
}

int ehReal(char *s, int i, int temPonto, int temDigito) {
    if (s[i] == '\0') return temPonto && temDigito;
    if (s[i] >= '0' && s[i] <= '9') return ehReal(s, i+1, temPonto, 1);
    if (s[i] == '.' && !temPonto)    return ehReal(s, i+1, 1, temDigito);
    return 0;
}

int main() {
    char linha[10000];
    while (fgets(linha, sizeof(linha), stdin)) {
        int len = strlen(linha);
        if (len > 0 && linha[len-1] == '\n') linha[--len] = '\0';
        if (strcmp(linha, "FIM") == 0) break;

        int sv = len > 0 && somenteVogais(linha, 0);
        int sc = len > 0 && somenteConsoantes(linha, 0);
        int ei = ehInt(linha, 0, 0);
        int er = ehReal(linha, 0, 0, 0);

        printf("%s %s %s %s\n",
            sv ? "SIM" : "NAO",
            sc ? "SIM" : "NAO",
            ei ? "SIM" : "NAO",
            er ? "SIM" : "NAO");
    }
    return 0;
}

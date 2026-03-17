# TP01Q10 - Is (RECURSIVO)

## Enunciado

Refazer a questão Is (Q03) de forma **recursiva em C**. Mesma lógica de classificação, mas implementada recursivamente.

**Saída:** `X1 X2 X3 X4` — ver Q03 para descrição completa.

---

## Lógica recursiva para cada função

### somenteVogais(s, i)
```
somenteVogais("", 0)    → true  (base: string vazia já validada)
somenteVogais(s, i):
  se s[i] for '\0' → true
  se s[i] não for vogal ASCII → false
  senão → somenteVogais(s, i+1)
```

### ehInt(s, i)
```
ehInt("", 0) → false  (vazia não é inteiro)
ehInt(s, i):
  se s[i] for '\0' → true  (chegou ao fim, todos dígitos)
  se s[i] não for dígito → false
  senão → ehInt(s, i+1)
```

---

## Observações

- **Acentos e cedilha ignorados** — só ASCII `A-Z`/`a-z` contam para vogal/consoante
- Mesmo comportamento do Is Iterativo, apenas a implementação é recursiva

> **Atenção:** O pub.out atual desta pasta parece conter o output da questão AlteracaoAleatoria (substituição de letras). Verifique se o pub.out foi atualizado antes de comparar.

---

## Solução (C — recursivo)

```c
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
    return somenteVogais(s, i+1);
}

int somenteConsoantes(char *s, int i) {
    if (s[i] == '\0') return 1;
    if (!ehLetraASCII(s[i]) || ehVogal(s[i])) return 0;
    return somenteConsoantes(s, i+1);
}

int ehInt(char *s, int i) {
    if (s[i] == '\0') return i > 0;
    if (s[i] < '0' || s[i] > '9') return 0;
    return ehInt(s, i+1);
}

int ehReal(char *s, int i, int temPonto, int temDigito) {
    if (s[i] == '\0') return temPonto && temDigito;
    if (s[i] >= '0' && s[i] <= '9') return ehReal(s, i+1, temPonto, 1);
    if (s[i] == '.' && !temPonto) return ehReal(s, i+1, 1, temDigito);
    return 0;
}

int main() {
    char linha[10000];
    while (fgets(linha, sizeof(linha), stdin)) {
        int len = strlen(linha);
        if (len > 0 && linha[len-1] == '\n') linha[--len] = '\0';
        if (strcmp(linha, "FIM") == 0) break;
        int sv = strlen(linha) > 0 && somenteVogais(linha, 0);
        int sc = strlen(linha) > 0 && somenteConsoantes(linha, 0);
        printf("%s %s %s %s\n",
            sv ? "SIM" : "NÃO",
            sc ? "SIM" : "NÃO",
            ehInt(linha, 0) ? "SIM" : "NÃO",
            ehReal(linha, 0, 0, 0) ? "SIM" : "NÃO");
    }
    return 0;
}
```

---

## Como testar

```bash
gcc IsRecursivo.c -o is_rec
./is_rec < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

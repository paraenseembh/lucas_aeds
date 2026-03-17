# TP01Q05 - Soma de Dígitos (ITERATIVO)

## Enunciado

Criar um método **iterativo em C** que recebe um número inteiro e retorna a soma de seus dígitos. Para cada linha de entrada, imprimir a soma. Leitura termina com `"FIM"`.

**Entrada:** um número inteiro por linha, terminada por `"FIM"`
**Saída:** a soma dos dígitos, uma por linha

---

## Análise do pub.in / pub.out

| Entrada | Soma | Cálculo          |
|---------|------|------------------|
| `784`   | `19` | 7+8+4 = 19       |
| `152`   | `8`  | 1+5+2 = 8        |
| `987`   | `24` | 9+8+7 = 24       |
| `12345` | `15` | 1+2+3+4+5 = 15   |

---

## Armadilhas

- A entrada é uma string — não converter para `int` antes de somar, pois perde os dígitos individuais
- Iterar sobre cada caractere e subtrair `'0'` para obter o valor do dígito

---

## Solução (C — iterativo)

```c
#include <stdio.h>
#include <string.h>

int somaDigitos(char *s) {
    int soma = 0;
    for (int i = 0; s[i] != '\0'; i++) {
        if (s[i] >= '0' && s[i] <= '9')
            soma += s[i] - '0';
    }
    return soma;
}

int main() {
    char linha[10000];
    while (fgets(linha, sizeof(linha), stdin)) {
        int len = strlen(linha);
        if (len > 0 && linha[len-1] == '\n') linha[--len] = '\0';
        if (strcmp(linha, "FIM") == 0) break;
        printf("%d\n", somaDigitos(linha));
    }
    return 0;
}
```

---

## Como testar

```bash
gcc SomaDigitos.c -o soma
./soma < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

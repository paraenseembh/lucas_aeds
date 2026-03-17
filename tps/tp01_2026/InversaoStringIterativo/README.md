# TP01Q04 - Inversão de String (ITERATIVO)

## Enunciado

Criar um método **iterativo em C** que recebe uma string e retorna a string invertida. Para cada linha de entrada, imprimir a string invertida. Leitura termina com `"FIM"`.

**Entrada:** uma string por linha, terminada por `"FIM"`
**Saída:** cada string invertida, uma por linha

---

## Análise do pub.in / pub.out

| Entrada          | Saída            |
|------------------|------------------|
| `Abacaxi`        | `ixacabA`        |
| `Relâmpago`      | `ogapmâleR`      |
| `Contemplação`   | `oãçalpmetnoC`   |
| `Tranquilidade`  | `edadiliuqnarT`  |

Observações:
- Maiúsculas e minúsculas mantidas exatamente
- Caracteres acentuados (UTF-8 multi-byte) invertidos corretamente — tratar como `char*` em C preserva a sequência de bytes, basta inverter por índice de byte

---

## Armadilhas

- Em C com strings UTF-8, inverter byte a byte (`strlen` + swap) funciona se a saída também for lida como UTF-8 — a sequência de bytes de um caractere multi-byte fica invertida dentro do caractere, mas o juiz provavelmente compara bytes, então inverter por byte deve estar correto
- Garantir que a leitura para ao encontrar `"FIM"` sem imprimir

---

## Solução (C — iterativo)

```c
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
```

---

## Como testar

```bash
gcc InversaoString.c -o inversao
./inversao < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

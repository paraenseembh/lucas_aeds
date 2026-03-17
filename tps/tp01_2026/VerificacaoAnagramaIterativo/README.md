# TP01Q06 - Verificação de Anagrama (ITERATIVO)

## Enunciado

Para cada linha de entrada, verificar se as duas strings separadas por `" - "` são anagramas. Imprimir `SIM` ou `NÃO`. Leitura termina com `"FIM"`.

**Entrada:** `string1 - string2` por linha, terminada por `"FIM"`
**Saída:** `SIM` ou `NÃO`

---

## Análise do pub.in / pub.out

| Entrada             | Saída | Motivo                                    |
|---------------------|-------|-------------------------------------------|
| `Amor - Roma`       | `SIM` | mesmas letras, ignorando maiúsculas       |
| `Escuta - Custa`    | `NÃO` | tamanhos diferentes (6 vs 5)              |
| `Feliz - Zielf`     | `SIM` | anagramas com maiúsculas misturadas       |
| `listen - silent`   | `SIM` | clássico                                  |
| `Glaven - Sotric`   | `NÃO` | letras distintas                          |

---

## Algoritmo (contagem de frequência)

1. Parsear a linha com o delimitador `" - "` (espaço-hífen-espaço)
2. Se os comprimentos diferirem → NÃO
3. Contar frequência de cada char (case-insensitive) na string 1
4. Decrementar pela frequência da string 2
5. Se algum contador ≠ 0 → NÃO

---

## Armadilhas

- O delimitador é `" - "` (com espaços), não apenas `"-"`
- Comparação **case-insensitive**: `'A'` e `'a'` são iguais
- Acentuados como `ã`, `é` — tratar como char com codepoint, não apenas ASCII
- Usar array de 65536 posições (ou `HashMap`) para suportar caracteres Unicode
- `"NÃO"` — atenção ao encoding na saída (usar MyIO ou UTF-8)

---

## Solução (Java — iterativo)

```java
public class VerificacaoAnagramaIterativo {

    static boolean saoAnagramas(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        int[] contador = new int[65536];
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            if (c >= 'A' && c <= 'Z') c = (char)(c + 32);
            contador[c]++;
        }
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (c >= 'A' && c <= 'Z') c = (char)(c + 32);
            contador[c]--;
        }
        for (int i = 0; i < contador.length; i++) {
            if (contador[i] != 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            String s1 = "", s2 = "";
            boolean achouDelim = false;
            for (int i = 0; i < linha.length(); i++) {
                if (!achouDelim && linha.charAt(i) == ' '
                        && i + 2 < linha.length()
                        && linha.charAt(i+1) == '-'
                        && linha.charAt(i+2) == ' ') {
                    achouDelim = true;
                    i += 2;
                    continue;
                }
                if (!achouDelim) s1 += linha.charAt(i);
                else s2 += linha.charAt(i);
            }
            MyIO.println(saoAnagramas(s1, s2) ? "SIM" : "NÃO");
            linha = MyIO.readLine();
        }
    }
}
```

---

## Como testar

```bash
javac VerificacaoAnagramaIterativo.java
java VerificacaoAnagramaIterativo < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

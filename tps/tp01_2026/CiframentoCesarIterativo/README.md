# TP01Q01 - Ciframento de César (ITERATIVO)

## Enunciado

Dado um conjunto de strings de entrada, cifrar cada uma usando o **Ciframento de César com deslocamento +3** e imprimir o resultado. A leitura termina quando a string `"FIM"` for encontrada (sem cifrá-la).

**Entrada:** uma string por linha, terminada por `"FIM"`
**Saída:** cada string cifrada, uma por linha

---

## Análise do pub.in / pub.out

| Entrada        | Saída          | Observação                                  |
|----------------|----------------|---------------------------------------------|
| `aça`          | `dêd`          | `a`→`d`, `ç`(U+00E7)→`ê`(U+00EA): +3 no codepoint |
| `Ada`          | `Dgd`          | maiúsculas também deslocadas                |
| `ata`          | `dwd`          | ASCII simples: +3 no byte                   |
| `"texto"`      | `—texto—`      | aspas curvas: usa byte CP1252               |

### Regra de cifragem

O deslocamento opera sobre o **valor de byte Windows-1252 (CP1252)** de cada caractere:

- Para a maioria dos caracteres (ASCII e Latin-1): equivalente a `codepoint + 3`
- Para caracteres tipográficos como aspas curvas e travessões, o valor CP1252 difere do codepoint Unicode

| Entrada    | CP1252 | +3   | Saída       |
|------------|--------|------|-------------|
| `"` U+201D | `0x94` | `0x97` | `—` U+2014 |
| `"` U+201C | `0x93` | `0x96` | `–` U+2013 |
| `–` U+2013 | `0x96` | `0x99` | `™` U+2122 |

---

## Armadilhas

1. **Encoding**: arquivos são UTF-8 → usar `InputStreamReader(System.in, "UTF-8")` e `PrintStream(System.out, true, "UTF-8")`; **não usar MyIO** (ISO-8859-1)
2. **Não usar `char + 3` diretamente**: falha para aspas curvas (`"` → `†` em vez de `—`)
3. **Unsigned byte**: usar `(bytes[0] & 0xFF) + 3` antes de fazer o cast

---

## Solução (Java — iterativo)

```java
import java.io.*;
import java.nio.charset.Charset;

class CiframentoCesar {

    static Charset CP1252 = Charset.forName("windows-1252");

    static char cifrarChar(char c) {
        byte[] bytes = String.valueOf(c).getBytes(CP1252);
        if (bytes.length == 1) {
            byte shifted = (byte)((bytes[0] & 0xFF) + 3);
            return new String(new byte[]{shifted}, CP1252).charAt(0);
        }
        return (char)(c + 3);
    }

    static String cifrar(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            sb.append(cifrarChar(s.charAt(i)));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        String linha = br.readLine();
        while (linha != null && !linha.equals("FIM")) {
            out.println(cifrar(linha));
            linha = br.readLine();
        }
    }
}
```

---

## Como testar

```bash
javac CiframentoCesar.java
java CiframentoCesar < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

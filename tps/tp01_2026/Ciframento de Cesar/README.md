# TP01Q01 - Ciframento de César

## Enunciado

Dado um conjunto de strings de entrada, cifrar cada uma delas usando o **Ciframento de César com deslocamento +3** e imprimir o resultado. A leitura termina quando a string `"FIM"` for encontrada (sem cifrá-la).

**Entrada:** uma string por linha, terminada por `"FIM"`
**Saída:** cada string cifrada, uma por linha

---

## Análise do pub.in / pub.out

| Entrada | Saída | Observação |
|---------|-------|------------|
| `aça`   | `dêd` | `a`→`d` (+3), `ç`(U+00E7)→`ê`(U+00EA) (+3 no codepoint) |
| `Ada`   | `Dgd` | maiúsculas também deslocadas |
| `ata`   | `dwd` | letras ASCII simples |
| `radar` | `udgdu` | sem tratamento especial para palíndromos |
| `"texto"` | `—texto—` | aspas curvas: usa valor de byte CP1252 |

### Regra de cifragem

O deslocamento é feito **pelo valor de byte Windows-1252 (CP1252)**, não pelo codepoint Unicode:

- Para caracteres que cabem em 1 byte CP1252 (praticamente todos do português):
  - Pegar o byte CP1252 do caractere
  - Somar 3
  - Converter o byte resultante de volta via CP1252

- Isso é equivalente a `codepoint + 3` para a maioria dos caracteres (ASCII e Latin-1),
  mas **difere para caracteres tipográficos** como aspas curvas e travessões:

| Char entrada | CP1252 | +3  | CP1252 resultado | Char saída |
|---|---|---|---|---|
| `"` (U+201D) | `0x94` | `0x97` | `—` (U+2014) | travessão |
| `"` (U+201C) | `0x93` | `0x96` | `–` (U+2013) | meia risca |
| `–` (U+2013) | `0x96` | `0x99` | `™` (U+2122) | marca registrada |
| `'` (U+2018) | `0x91` | `0x94` | `"` (U+201D) | aspas direitas |

---

## Armadilhas de implementação

### 1. Encoding dos arquivos
- `pub.in` e `pub.out` são **UTF-8**
- É necessário ler como UTF-8 e escrever como UTF-8
- **Não usar MyIO** (que usa ISO-8859-1) — causaria leitura incorreta dos caracteres acentuados

### 2. Não usar `char + 3` diretamente para todos os casos
- Para `ç` (U+00E7): `U+00E7 + 3 = U+00EA` (ê) ✓ — coincide com CP1252
- Para `"` (U+201D): `U+201D + 3 = U+2020` (†) ✗ — errado!
  - CP1252 correto: `0x94 + 3 = 0x97` → `—` ✓

### 3. Overflow de byte
- `(bytes[0] & 0xFF) + 3` — usar máscara `& 0xFF` para tratar o byte como unsigned
- Fazer o cast `(byte)` do resultado para passar para o construtor `new String(byte[], Charset)`

---

## Solução em Java

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

## Como compilar e testar

```bash
javac CiframentoCesar.java
java CiframentoCesar < pub.in > minha_saida.txt
diff pub.out minha_saida.txt  # sem diferenças = correto
```

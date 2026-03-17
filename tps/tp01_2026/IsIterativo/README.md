# TP01Q03 - Is (ITERATIVO)

## Enunciado

Para cada linha de entrada, classificar a string em 4 categorias e imprimir `SIM` ou `NÃO` para cada uma, separadas por espaço. Leitura termina com `"FIM"`.

**Saída:** `X1 X2 X3 X4` onde:
- `X1` = somente vogais?
- `X2` = somente consoantes?
- `X3` = número inteiro?
- `X4` = número real (com ponto decimal)?

**Observação importante:** desconsiderar acentos e cedilha — considerar apenas caracteres ASCII entre `'A'-'Z'` e `'a'-'z'` para classificar vogais/consoantes.

---

## Análise do pub.in

| Entrada     | Saída             | Raciocínio                                     |
|-------------|-------------------|------------------------------------------------|
| `aeiou`     | `SIM NÃO NÃO NÃO` | só vogais ASCII                                |
| `bcd`       | `NÃO SIM NÃO NÃO` | só consoantes                                  |
| `123`       | `NÃO NÃO SIM NÃO` | só dígitos → inteiro                           |
| `12.3`      | `NÃO NÃO NÃO SIM` | dígitos com ponto → real                       |
| `aça`       | `SIM NÃO NÃO NÃO` | `ç` ignorado, restam `a`,`a` → só vogais       |
| `Ada`       | `NÃO NÃO NÃO NÃO` | `A`, `d`, `a` → tem vogal e consoante, não é nenhuma das 4 |
| `hello`     | `NÃO NÃO NÃO NÃO` | tem vogais e consoantes juntas                 |

> **Atenção:** O pub.out atual desta pasta parece conter o output da questão AlteracaoAleatoria (substituição de letras), não os valores SIM/NÃO. Verifique se o pub.out foi atualizado antes de comparar.

---

## Armadilhas

- **Acentos e cedilha são ignorados** na classificação de vogais/consoantes: `ç`, `á`, `ê`, etc. não são letras para este propósito
- Apenas os 26 caracteres ASCII `a-z`/`A-Z` contam
- String vazia: `somenteVogais` = NÃO, `somenteConsoantes` = NÃO
- `ehReal` exige pelo menos um dígito E exatamente um ponto — `"."` → NÃO
- `ehInt` rejeita sinais negativos (`"-1"`) — depende do enunciado; verificar no pub.out

---

## Solução (Java — iterativo)

```java
public class VogaisConsoantes {

    static boolean ehVogal(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    static boolean ehLetraASCII(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    static char toLower(char c) {
        return (c >= 'A' && c <= 'Z') ? (char)(c + 32) : c;
    }

    static String somenteVogais(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = toLower(s.charAt(i));
            if (!ehLetraASCII(c) || !ehVogal(c)) return "NÃO";
        }
        return s.isEmpty() ? "NÃO" : "SIM";
    }

    static String somenteConsoantes(String s) {
        if (s.isEmpty()) return "NÃO";
        for (int i = 0; i < s.length(); i++) {
            char c = toLower(s.charAt(i));
            if (!ehLetraASCII(c) || ehVogal(c)) return "NÃO";
        }
        return "SIM";
    }

    static String ehInt(String s) {
        if (s.isEmpty()) return "NÃO";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') return "NÃO";
        }
        return "SIM";
    }

    static String ehReal(String s) {
        boolean temPonto = false, temDigito = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') temDigito = true;
            else if (c == '.') { if (temPonto) return "NÃO"; temPonto = true; }
            else return "NÃO";
        }
        return (temDigito && temPonto) ? "SIM" : "NÃO";
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(somenteVogais(linha) + " " + somenteConsoantes(linha)
                       + " " + ehInt(linha) + " " + ehReal(linha));
            linha = MyIO.readLine();
        }
    }
}
```

---

## Como testar

```bash
javac VogaisConsoantes.java
java VogaisConsoantes < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

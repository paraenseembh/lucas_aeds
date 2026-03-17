# TP01Q02 - Alteração Aleatória (ITERATIVO)

## Enunciado

Para cada linha de entrada, sortear **duas letras minúsculas aleatórias** usando `Random` com `seed=4`, substituir todas as ocorrências da primeira letra pela segunda na linha e imprimir o resultado. Leitura termina com `"FIM"`.

**Entrada:** uma string por linha, terminada por `"FIM"`
**Saída:** a string com a substituição aplicada

---

## Análise do pub.in / pub.out

O `Random` com `seed=4` é criado **uma única vez** e continua rolando a cada linha:

| Linha | Entrada   | Saída     | Substituição |
|-------|-----------|-----------|--------------|
| 1     | `aça`     | `aça`     | letra1 não aparece na string |
| 7     | `ama`     | `aga`     | `m` → `g` |
| 27    | `reviver` | `rfvivfr` | `e` → `f` |

A cada linha são consumidos **dois** valores do gerador:
```
letra1 = (char)('a' + Math.abs(r.nextInt()) % 26)
letra2 = (char)('a' + Math.abs(r.nextInt()) % 26)
```

---

## Armadilhas

- O `Random` **não** é reiniciado a cada linha — ele é criado uma vez antes do loop
- Se `letra1 == letra2`, a string fica inalterada
- A substituição é case-sensitive: só substitui letras minúsculas iguais a `letra1`
- Caracteres acentuados e maiúsculos não são afetados

---

## Solução (Java — iterativo)

```java
import java.util.Random;

class AlteracaoAleatoria {

    static char sorteiaLetra(Random r) {
        return (char)('a' + Math.abs(r.nextInt()) % 26);
    }

    static String alterar(String s, char de, char para) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sb.append(c == de ? para : c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Random r = new Random();
        r.setSeed(4);
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            char letra1 = sorteiaLetra(r);
            char letra2 = sorteiaLetra(r);
            MyIO.println(alterar(linha, letra1, letra2));
            linha = MyIO.readLine();
        }
    }
}
```

---

## Como testar

```bash
javac AlteracaoAleatoria.java
java AlteracaoAleatoria < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

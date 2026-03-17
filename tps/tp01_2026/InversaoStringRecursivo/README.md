# TP01Q11 - Inversão de String (RECURSIVO)

## Enunciado

Refazer a questão Inversão de String (Q04) de forma **recursiva em Java**. Para cada linha de entrada, imprimir a string invertida. Leitura termina com `"FIM"`.

**Entrada:** uma string por linha, terminada por `"FIM"`
**Saída:** cada string invertida, uma por linha

---

## Análise do pub.in / pub.out

Mesmo pub.in/pub.out da versão iterativa (Q04):

| Entrada        | Saída          |
|----------------|----------------|
| `Abacaxi`      | `ixacabA`      |
| `Relâmpago`    | `ogapmâleR`    |
| `Contemplação` | `oãçalpmetnoC` |

---

## Lógica recursiva

```
inverter("abc") = inverter("bc") + "a"
inverter("a")   = "a"
inverter("")    = ""
```

Ou equivalentemente:
```
inverter(s) = s.charAt(s.length()-1) + inverter(s.substring(0, s.length()-1))
```

---

## Solução (Java — recursivo)

```java
class InversaoStringRecursivo {

    static String inverter(String s) {
        if (s.length() <= 1) return s;
        return inverter(s.substring(1)) + s.charAt(0);
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(inverter(linha));
            linha = MyIO.readLine();
        }
    }
}
```

---

## Como testar

```bash
javac InversaoStringRecursivo.java
java InversaoStringRecursivo < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

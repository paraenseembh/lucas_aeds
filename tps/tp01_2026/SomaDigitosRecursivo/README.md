# TP01Q12 - Soma de Dígitos (RECURSIVO)

## Enunciado

Refazer a questão Soma de Dígitos (Q05) de forma **recursiva em Java**. Mesmo comportamento, implementação recursiva.

**Entrada:** um número inteiro por linha, terminada por `"FIM"`
**Saída:** a soma dos dígitos, uma por linha

---

## Lógica recursiva

```
somaDigitos("784") = 7 + somaDigitos("84")
somaDigitos("84")  = 8 + somaDigitos("4")
somaDigitos("4")   = 4 + somaDigitos("")
somaDigitos("")    = 0  (base)
```

Ou usando índice:
```
somaDigitos(s, i):
  se i == s.length() → 0
  senão → (s[i] - '0') + somaDigitos(s, i+1)
```

---

## Solução (Java — recursivo)

```java
class SomaDigitosRecursivo {

    static int somaDigitos(String s, int i) {
        if (i == s.length()) return 0;
        char c = s.charAt(i);
        int digito = (c >= '0' && c <= '9') ? c - '0' : 0;
        return digito + somaDigitos(s, i + 1);
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(somaDigitos(linha, 0));
            linha = MyIO.readLine();
        }
    }
}
```

---

## Como testar

```bash
javac SomaDigitosRecursivo.java
java SomaDigitosRecursivo < pub.in > minha_saida.txt
diff pub.out minha_saida.txt
```

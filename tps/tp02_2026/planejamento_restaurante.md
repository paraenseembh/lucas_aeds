# Planejamento — Modelagem de Dados: Restaurante

## Objetivo

Implementar os tipos `Data`, `Hora`, `Restaurante` e `ColecaoRestaurantes` em Java (Q1) e C (Q2), servindo de base para todas as questões seguintes do TP02.

---

## Tipos e Campos

### `Data`
| Campo | Tipo   |
|-------|--------|
| ano   | int    |
| mes   | int    |
| dia   | int    |

### `Hora`
| Campo  | Tipo |
|--------|------|
| hora   | int  |
| minuto | int  |

### `Restaurante`
| Campo              | Tipo           | Origem no CSV         |
|--------------------|----------------|-----------------------|
| id                 | int            | campo 0               |
| nome               | String/char[]  | campo 1               |
| cidade             | String/char[]  | campo 2               |
| capacidade         | int            | campo 3               |
| avaliacao          | double         | campo 4               |
| tiposCozinha       | String[]/char[][]| campo 5, separador `;` |
| faixaPreco         | int (1–4)      | campo 6, `strlen`/`.length()` |
| horarioAbertura    | Hora           | campo 7, antes do `-` |
| horarioFechamento  | Hora           | campo 7, depois do `-`|
| dataAbertura       | Data           | campo 8, formato `YYYY-MM-DD` |
| aberto             | boolean/int    | campo 9               |

### `ColecaoRestaurantes`
| Campo       | Tipo               |
|-------------|--------------------|
| tamanho     | int                |
| restaurantes| Restaurante[]/Restaurante*[] |

---

## Regras de Parsing do CSV

- Separador de campos: `,`
- `tipos_cozinha` (campo 5): separar por `;` em arranjo de strings
- `faixa_preco` (campo 6): contar o número de `$` (ex: `"$$"` → `2`)
- `horario` (campo 7, ex: `"12:00-20:00"`): split por `-` → `horarioAbertura` e `horarioFechamento`
- `data_abertura` (campo 8, ex: `"2020-07-12"`): split por `-` → ano, mes, dia
- `aberto` (campo 9): `"true"` → `true`/`1`, caso contrário `false`/`0`
- Remover `\r\n` trailing em C (usar loop `while` no fim do buffer)

---

## Formato de Saída

```
[id ## nome ## cidade ## capacidade ## avaliacao ## [t1,t2] ## $$ ## HH:mm-HH:mm ## DD/MM/YYYY ## aberto]
```

**Exemplo:**
```
[478 ## Le Gourmet 478 ## Paris ## 178 ## 4.8 ## [francesa,bistro] ## $$$ ## 12:00-20:00 ## 12/07/2020 ## false]
```

### Atenção ao separador decimal
- Java: `String.format(Locale.US, "%.1f", avaliacao)`
- C: `setlocale(LC_NUMERIC, "C")` no início do `main`

---

## Interface em Java (Q1)

```java
class Data {
    // atributos privados: ano, mes, dia
    public static Data parseData(String s)   // "YYYY-MM-DD" → Data
    public String formatar()                 // → "DD/MM/YYYY"
}

class Hora {
    // atributos privados: hora, minuto
    public static Hora parseHora(String s)   // "HH:mm" → Hora
    public String formatar()                 // → "HH:mm"
}

class Restaurante {
    // atributos privados (todos os campos)
    public static Restaurante parseRestaurante(String s)
    public String formatar()
    // getters: getId(), getNome(), getCidade(), getCapacidade(),
    //          getAvaliacao(), getDataAbertura(), ...
}

class ColecaoRestaurantes {
    public Restaurante[] getRestaurantes()
    public void lerCsv(String path)
    public static ColecaoRestaurantes lerCsv()  // lê de /tmp/restaurantes.csv
}
```

---

## Interface em C (Q2)

```c
typedef struct { int ano, mes, dia; } Data;
typedef struct { int hora, minuto; } Hora;
typedef struct {
    int id; char nome[100]; char cidade[100];
    int capacidade; double avaliacao;
    char tipos_cozinha[5][30]; int num_tipos;
    int faixa_preco;
    Hora horario_abertura; Hora horario_fechamento;
    Data data_abertura; int aberto;
} Restaurante;

typedef struct {
    int tamanho;
    Restaurante* restaurantes[600];
} Colecao_Restaurantes;

void formatar_data(Data* d, char* buf);           // → "DD/MM/YYYY"
void formatar_hora(Hora* h, char* buf);           // → "HH:mm"
Restaurante* parse_restaurante(char* s);          // aloca com malloc
void formatar_restaurante(Restaurante* r, char* buf);
void ler_csv_colecao(Colecao_Restaurantes* col, char* path);
```

---

## Fluxo do Programa (Q1 e Q2)

```
1. lerCsv("/tmp/restaurantes.csv")  →  coleção com todos os restaurantes
2. loop: ler id da stdin
   - se id == -1: encerrar
   - buscar restaurante pelo id na coleção
   - imprimir restaurante.formatar()
```

---

## Armadilhas Conhecidas

| Problema | Causa | Solução |
|----------|-------|---------|
| `strtok` interleave em C | não dá para usar strtok com `,` e `;` simultâneo | copiar campo 5 para buffer separado antes de chamar strtok com `;` |
| `\r` no final dos campos | CSV com CRLF no Windows | loop `while(buf[len-1]=='\r'||buf[len-1]=='\n') buf[--len]='\0'` |
| Separador decimal `.` vs `,` | locale do sistema | Java: `Locale.US`; C: `setlocale(LC_NUMERIC,"C")` |
| Arquivo não encontrado em Windows | `/tmp/` não existe | para testes locais: criar `C:/tmp/` e copiar CSV; no juiz Linux funciona normalmente |

---

## Estrutura de Arquivos

```
tp02_2026/
├── restaurantes.csv
├── TP02Q01 - Modelagem em Java/
│   ├── Q1.java
│   ├── pub.in
│   └── pub.out
├── TP02Q02 - Modelagem em C/
│   ├── Q2.c
│   ├── pub.in
│   └── pub.out
└── planejamento_restaurante.md   ← este arquivo
```

---

## Compilação e Teste

```bash
# Java
javac Q1.java && java Q1 < pub.in | diff - pub.out

# C
gcc Q2.c -o q2 && ./q2 < pub.in | diff - pub.out
```

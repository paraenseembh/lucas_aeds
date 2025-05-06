# Guia de Implementação para Processamento de Dados do Disney+ CSV

Este guia fornece instruções detalhadas sobre como implementar soluções para as questões do Trabalho Prático II relacionadas ao processamento de dados do arquivo `disneyplus.csv`. Ele é baseado na implementação encontrada no arquivo TP02Q07.java, que demonstra um excelente processamento de dados.

## 1. Estrutura da Classe Show

### Atributos
```java
// Atributos básicos
private String show_id;
private String type;
private String title;
private String director;
private String cast;
private String country;
private String date_added;
private String release_year;
private String rating;
private String duration;
private String listed_in;
private String description;

// Atributos processados
private String[] elenco;        // Array do elenco (cast) ordenado
private String[] listados;      // Array das categorias (listed_in) ordenado
private int tam_elenco;         // Tamanho do array de elenco
private int tam_listados;       // Tamanho do array de categorias
private int dia, mes, ano;      // Componentes da data
private boolean tem_data;       // Indicador se tem data válida
```

### Construtores

Implemente dois construtores:
1. **Construtor padrão**: inicializa todos os atributos com valores vazios ou nulos
2. **Construtor com parâmetros**: recebe todos os atributos básicos e processa os atributos derivados

### Métodos Essenciais

- **clone()**: cria uma cópia exata do objeto
- **ler(String linha)**: método estático para criar um objeto Show a partir de uma linha CSV
- **imprimir()**: exibe os dados do show no formato solicitado
- **getters e setters**: para todos os atributos básicos

## 2. Processamento de Dados

### Leitura do Arquivo CSV

```java
private static int lerArquivoCSV(Show[] shows) {
    int totalShows = 0;
    
    try (BufferedReader br = new BufferedReader(new FileReader("/tmp/disneyplus.csv"))) {
        // Pula a linha de cabeçalho
        String linha = br.readLine();
        
        // Lê cada linha e cria um show
        while ((linha = br.readLine()) != null && totalShows < MAX_SHOWS) {
            shows[totalShows] = Show.ler(linha);
            totalShows++;
        }
    } catch (IOException e) {
        System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
    }
    
    return totalShows;
}
```

### Processamento de Linhas CSV

O método abaixo é crucial para lidar corretamente com campos que contêm vírgulas dentro de aspas:

```java
private static String[] extrairCampos(String linha) {
    ArrayList<String> campos = new ArrayList<>();
    
    StringBuilder campo = new StringBuilder();
    boolean dentroAspas = false;
    
    for (int i = 0; i < linha.length(); i++) {
        char c = linha.charAt(i);
        
        if (c == '"') {
            dentroAspas = !dentroAspas;
        } else if (c == ',' && !dentroAspas) {
            // Encontrou um delimitador fora de aspas
            if (campo.length() == 0) {
                campos.add("NaN");
            } else {
                campos.add(campo.toString());
            }
            campo = new StringBuilder();
        } else {
            campo.append(c);
        }
    }
    
    // Adiciona o último campo
    if (campo.length() == 0) {
        campos.add("NaN");
    } else {
        campos.add(campo.toString());
    }
    
    // Garantir que temos 12 campos
    while (campos.size() < 12) {
        campos.add("NaN");
    }
    
    return campos.toArray(new String[0]);
}
```

### Processamento de Arrays (elenco e categorias)

Para processar e ordenar strings separadas por vírgulas:

```java
private String[] separaString(String str) {
    if (str == null || str.equals("NaN")) {
        return new String[0];
    }
    
    // Dividir a string por vírgulas
    String[] resultado = str.split(",");
    
    // Remover espaços extras e aspas
    for (int i = 0; i < resultado.length; i++) {
        resultado[i] = resultado[i].trim().replace("\"", "");
    }
    
    // Ordenar o array
    Arrays.sort(resultado);
    
    return resultado;
}
```

### Processamento de Datas

```java
private void processaData() {
    tem_data = false;
    if (!date_added.equals("NaN")) {
        try {
            String[] partes = date_added.split(",");
            if (partes.length >= 2) {
                String[] mesdia = partes[0].trim().split(" ");
                String mes_str = mesdia[0].trim();
                dia = Integer.parseInt(mesdia[1].trim());
                ano = Integer.parseInt(partes[1].trim());
                
                Map<String, Integer> meses = new HashMap<>();
                meses.put("January", 1);
                meses.put("February", 2);
                meses.put("March", 3);
                meses.put("April", 4);
                meses.put("May", 5);
                meses.put("June", 6);
                meses.put("July", 7);
                meses.put("August", 8);
                meses.put("September", 9);
                meses.put("October", 10);
                meses.put("November", 11);
                meses.put("December", 12);
                
                mes = meses.getOrDefault(mes_str, 0);
                
                if (mes > 0) {
                    tem_data = true;
                }
            }
        } catch (Exception e) {
            tem_data = false;
        }
    }
}
```

### Formato de Impressão

```java
public void imprimir() {
    System.out.print("=> " + show_id + " ## " + title + " ## " + type + " ## " + director + " ## ");
    imprimirArray(elenco);
    System.out.print(" ## " + country + " ## " + date_added + " ## " + release_year + " ## " + 
                    rating + " ## " + duration + " ## ");
    imprimirArray(listados);
    System.out.println(" ## ");
}

private void imprimirArray(String[] array) {
    System.out.print("[");
    if (array != null && array.length > 0) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
    }
    System.out.print("]");
}
```

## 3. Algoritmos de Ordenação

### Ordenação por Inserção

Exemplo de implementação do algoritmo de inserção com contadores de comparações e movimentações:

```java
private static void ordenacaoInsercao(Show[] shows, int comparacoes, int movimentacoes) {
    for (int i = 1; i < shows.length; i++) {
        Show tmp = shows[i];
        int j = i - 1;
        
        while (j >= 0) {
            comparacoes++;
            int comp = shows[j].getType().compareTo(tmp.getType());
            
            // Se types forem iguais, compara por title (critério de desempate)
            if (comp == 0) {
                comparacoes++;
                comp = shows[j].getTitle().compareTo(tmp.getTitle());
            }
            
            if (comp > 0) {
                shows[j + 1] = shows[j];
                movimentacoes++;
                j--;
            } else {
                break;
            }
        }
        
        shows[j + 1] = tmp;
        movimentacoes++;
    }
}
```

## 4. Geração de Logs

```java
private static void gerarLog(String nomeArquivo, int comparacoes, int movimentacoes, long inicio, long fim) {
    try (FileWriter fw = new FileWriter(nomeArquivo)) {
        fw.write("1404192\t");
        fw.write(comparacoes + "\t");
        fw.write(movimentacoes + "\t");
        fw.write(((fim - inicio) / 1000.0) + "s\t");
    } catch (IOException e) {
        System.out.println("Erro ao gerar arquivo de log: " + e.getMessage());
    }
}
```

## 5. Estrutura Básica do Programa Principal

```java
public static void main(String[] args) {
    // Lê todos os shows do arquivo CSV
    Show[] shows = new Show[MAX_SHOWS];
    int totalShows = lerArquivoCSV(shows);
    
    // Lê as entradas da entrada padrão
    Scanner sc = new Scanner(System.in);
    ArrayList<Show> meusShows = new ArrayList<>();
    
    String id;
    while (!(id = sc.nextLine()).equals("FIM")) {
        for (int i = 0; i < totalShows; i++) {
            if (shows[i].getShowId().equals(id)) {
                meusShows.add(shows[i]);
                break;
            }
        }
    }
    
    // Converte ArrayList para array
    Show[] showsParaOrdenar = meusShows.toArray(new Show[0]);
    
    // Inicializa contadores
    int comparacoes = 0;
    int movimentacoes = 0;
    
    // Registra tempo de início
    long inicio = System.currentTimeMillis();
    
    // Executa o algoritmo de ordenação
    // ordenacaoXXX(showsParaOrdenar, comparacoes, movimentacoes);
    
    // Registra tempo de fim
    long fim = System.currentTimeMillis();
    
    // Imprime os shows ordenados
    for (Show show : showsParaOrdenar) {
        show.imprimir();
    }
    
    // Gera arquivo de log
    gerarLog("1404192_.txt", comparacoes, movimentacoes, inicio, fim);
    
    sc.close();
}
```

## 6. Dicas para Implementação em C

Para a implementação em C, é necessário adaptar o código considerando:

1. **Estrutura de dados**: Usar `struct` para representar o Show
2. **Alocação de memória**: Alocar dinamicamente os arrays para elenco e categorias
3. **Manipulação de strings**: Usar funções como `strtok`, `strcpy` e `strcmp`
4. **Ordenação de arrays**: Implementar funções de ordenação para os arrays de strings

Exemplo de estrutura em C:

```c
typedef struct {
    char show_id[20];
    char type[20];
    char title[200];
    char director[200];
    char* cast;
    char** elenco;
    int tam_elenco;
    // ... outros campos
} Show;
```

## 7. Considerações Importantes

1. **Tratamento de valores ausentes**: Sempre substituir campos vazios por "NaN"
2. **Ordem de impressão**: Seguir exatamente o formato especificado
3. **Ordenação de arrays**: Garantir que os arrays de elenco e categorias estejam ordenados
4. **Critérios de desempate**: Implementar corretamente os critérios de desempate nos algoritmos de ordenação
5. **Campos com vírgulas**: Tratar corretamente campos que contêm vírgulas dentro de aspas

Este guia deve fornecer uma base sólida para implementar todas as questões do trabalho, aproveitando o excelente processamento de dados do arquivo TP02Q07.java.
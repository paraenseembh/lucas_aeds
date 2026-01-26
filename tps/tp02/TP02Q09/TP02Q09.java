/**
 * Implementação de Heapsort para ordenação de shows do Disney+
 * @author Lucas Nascimento
 * Matrícula: 1404192
 */

 import java.io.*;
 import java.util.*;
 
 class Show {
     // Atributos do show
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
     private String[] elenco;
     private String[] listados;
     private int tam_elenco;
     private int tam_listados;
     private int dia, mes, ano;
     private boolean tem_data;
 
     /**
      * Construtor padrão
      */
     public Show() {
         this.show_id = "";
         this.type = "";
         this.title = "";
         this.director = "";
         this.cast = "";
         this.country = "";
         this.date_added = "";
         this.release_year = "";
         this.rating = "";
         this.duration = "";
         this.listed_in = "";
         this.description = "";
         this.elenco = null;
         this.listados = null;
         this.tam_elenco = 0;
         this.tam_listados = 0;
         this.tem_data = false;
     }
 
     /**
      * Construtor com parâmetros
      */
     public Show(String show_id, String type, String title, String director, String cast,
                 String country, String date_added, String release_year, String rating,
                 String duration, String listed_in, String description) {
         this.show_id = show_id;
         this.type = type;
         this.title = title;
         this.director = director;
         this.cast = cast;
         this.country = country;
         this.date_added = date_added;
         this.release_year = release_year;
         this.rating = rating;
         this.duration = duration;
         this.listed_in = listed_in;
         this.description = description;
         
         // Processamento de elenco e categorias
         this.elenco = separaString(cast);
         this.tam_elenco = this.elenco.length;
         
         this.listados = separaString(listed_in);
         this.tam_listados = this.listados.length;
         
         // Processamento de data
         processaData();
     }
 
     /**
      * Processa a data de lançamento
      */
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
 
     /**
      * Separa uma string em um array usando vírgula como delimitador
      * @param str String a ser separada
      * @return Array de strings ordenado
      */
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
 
     /**
      * Cria uma cópia do objeto Show
      * @return Nova instância de Show com os mesmos valores
      */
     public Show clone() {
         return new Show(
             this.show_id, this.type, this.title, this.director, this.cast,
             this.country, this.date_added, this.release_year, this.rating,
             this.duration, this.listed_in, this.description
         );
     }
 
     /**
      * Lê os dados de um show a partir de uma linha CSV
      * @param linha Linha contendo os dados do show
      * @return Instância de Show preenchida
      */
     public static Show ler(String linha) {
         String[] campos = extrairCampos(linha);
         return new Show(
             campos[0], campos[1], campos[2], campos[3], campos[4],
             campos[5], campos[6], campos[7], campos[8], campos[9],
             campos[10], campos[11]
         );
     }
 
     /**
      * Extrai campos de uma linha CSV respeitando aspas
      * @param linha Linha CSV a ser processada
      * @return Array com os campos extraídos
      */
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
 
     /**
      * Imprime os dados do show no formato especificado
      * Corrigido para mostrar title antes de type
      */
     public void imprimir() {
         System.out.print("=> " + show_id + " ## " + title + " ## " + type + " ## " + director + " ## ");
         imprimirArray(elenco);
         System.out.print(" ## " + country + " ## " + date_added + " ## " + release_year + " ## " + 
                          rating + " ## " + duration + " ## ");
         imprimirArray(listados);
         System.out.println(" ## ");
     }
 
     /**
      * Imprime um array de strings no formato [item1, item2, ...]
      * @param array Array a ser impresso
      */
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
 
     // Getters
     public String getShowId() { return show_id; }
     public String getType() { return type; }
     public String getTitle() { return title; }
     public String getDirector() { return director; }
     public String getCast() { return cast; }
     public String getCountry() { return country; }
     public String getDateAdded() { return date_added; }
     public String getReleaseYear() { return release_year; }
     public String getRating() { return rating; }
     public String getDuration() { return duration; }
     public String getListedIn() { return listed_in; }
     public String getDescription() { return description; }
     
     // Setters
     public void setShowId(String show_id) { this.show_id = show_id; }
     public void setType(String type) { this.type = type; }
     public void setTitle(String title) { this.title = title; }
     public void setDirector(String director) { this.director = director; }
     public void setCast(String cast) { 
         this.cast = cast;
         this.elenco = separaString(cast);
         this.tam_elenco = this.elenco.length;
     }
     public void setCountry(String country) { this.country = country; }
     public void setDateAdded(String date_added) { 
         this.date_added = date_added; 
         processaData();
     }
     public void setReleaseYear(String release_year) { this.release_year = release_year; }
     public void setRating(String rating) { this.rating = rating; }
     public void setDuration(String duration) { this.duration = duration; }
     public void setListedIn(String listed_in) { 
         this.listed_in = listed_in; 
         this.listados = separaString(listed_in);
         this.tam_listados = this.listados.length;
     }
     public void setDescription(String description) { this.description = description; }
 }
 
 public class TP02Q09 {
     // Constantes
     private static final int MAX_SHOWS = 1368;
     private static final String CSV_PATH = "/tmp/disneyplus.csv";
     private static int comparacoes = 0;
     private static int movimentacoes = 0;
     
     /**
      * Método principal
      */
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
         
         // Ordena usando heapsort
         comparacoes = 0;
         movimentacoes = 0;
         
         long inicio = System.currentTimeMillis();
         heapsort(showsParaOrdenar);
         long fim = System.currentTimeMillis();
         
         // Imprime os shows ordenados
         for (Show show : showsParaOrdenar) {
             show.imprimir();
         }
         
         // Gera arquivo de log
         gerarLog("matricula_heapsort.txt", comparacoes, movimentacoes, inicio, fim);
         
         sc.close();
     }
     
     /**
      * Lê o arquivo CSV e preenche o array de shows
      * @param shows Array para armazenar os shows
      * @return Número total de shows lidos
      */
     private static int lerArquivoCSV(Show[] shows) {
         int totalShows = 0;
         
         try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
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
     
     /**
      * Compara dois shows por director e title (desempate)
      * @param a Primeiro show
      * @param b Segundo show
      * @return Valor negativo se a < b, 0 se iguais, positivo se a > b
      */
     private static int compararShows(Show a, Show b) {
         comparacoes++;
         
         // Tratar casos onde director é "NaN"
         String dirA = a.getDirector();
         String dirB = b.getDirector();
         
         if (dirA.equals("NaN") && !dirB.equals("NaN")) {
             return 1; // NaN vem depois de qualquer string
         } else if (!dirA.equals("NaN") && dirB.equals("NaN")) {
             return -1; // Qualquer string vem antes de NaN
         } else if (dirA.equals("NaN") && dirB.equals("NaN")) {
             // Se ambos são NaN, desempata por título
             comparacoes++;
             return a.getTitle().compareTo(b.getTitle());
         }
         
         int cmp = dirA.compareTo(dirB);
         
         // Em caso de empate, compara por title
         if (cmp == 0) {
             comparacoes++;
             cmp = a.getTitle().compareTo(b.getTitle());
         }
         
         return cmp;
     }
     
     /**
      * Algoritmo de ordenação Heapsort
      * @param shows Array de shows a ser ordenado
      */
     private static void heapsort(Show[] shows) {
         int n = shows.length;
         
         // Construir heap (reorganizar o array)
         for (int i = n / 2 - 1; i >= 0; i--) {
             reconstruirHeap(shows, n, i);
         }
         
         // Extrair um elemento por vez do heap
         for (int i = n - 1; i > 0; i--) {
             // Move a raiz atual para o final
             Show temp = shows[0];
             shows[0] = shows[i];
             shows[i] = temp;
             movimentacoes++;
             
             // Reconstrói o heap para o array reduzido
             reconstruirHeap(shows, i, 0);
         }
     }
     
     /**
      * Reconstrói o heap a partir de um nó
      * @param shows Array de shows
      * @param n Tamanho do heap
      * @param i Índice do nó a reconstruir
      */
     private static void reconstruirHeap(Show[] shows, int n, int i) {
         int maior = i; // Inicializa o maior como raiz
         int esq = 2 * i + 1; // Filho esquerdo
         int dir = 2 * i + 2; // Filho direito
         
         // Se o filho esquerdo é maior que a raiz
         if (esq < n && compararShows(shows[esq], shows[maior]) > 0) {
             maior = esq;
         }
         
         // Se o filho direito é maior que o maior até agora
         if (dir < n && compararShows(shows[dir], shows[maior]) > 0) {
             maior = dir;
         }
         
         // Se o maior não é a raiz
         if (maior != i) {
             Show swap = shows[i];
             shows[i] = shows[maior];
             shows[maior] = swap;
             movimentacoes++;
             
             // Reconstrói recursivamente o heap afetado
             reconstruirHeap(shows, n, maior);
         }
     }
     
     /**
      * Gera arquivo de log com os dados da execução
      * @param nomeArquivo Nome do arquivo de log
      * @param comparacoes Número de comparações realizadas
      * @param movimentacoes Número de movimentações realizadas
      * @param inicio Timestamp de início da ordenação
      * @param fim Timestamp de fim da ordenação
      */
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
 }
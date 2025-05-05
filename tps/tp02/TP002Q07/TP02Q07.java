/**
 * Programa de manipulação e ordenação de shows do Disney+
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
      */
     public void imprimir() {
         System.out.print("=> " + show_id + " ## " + type + " ## " + title + " ## " + director + " ## ");
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
 
 public class TP02Q07 {
     // Constantes
     private static final int MAX_SHOWS = 1368;
     private static final String CSV_PATH = "/tmp/disneyplus.csv";
     
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
         
         // Ordena usando inserção
         int comparacoes = 0;
         int movimentacoes = 0;
         
         long inicio = System.currentTimeMillis();
         ordenacaoInsercao(showsParaOrdenar, comparacoes, movimentacoes);
         long fim = System.currentTimeMillis();
         
         // Imprime os shows ordenados
         for (Show show : showsParaOrdenar) {
             show.imprimir();
         }
         
         // Gera arquivo de log
         gerarLog("1404192_insercao.txt", comparacoes, movimentacoes, inicio, fim);
         
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
      * Algoritmo de ordenação por inserção
      * Ordenação por type e usando title como critério de desempate
      * @param shows Array de shows a ser ordenado
      * @param comparacoes Contador de comparações (passado por referência)
      * @param movimentacoes Contador de movimentações (passado por referência)
      */
     private static void ordenacaoInsercao(Show[] shows, int comparacoes, int movimentacoes) {
         for (int i = 1; i < shows.length; i++) {
             Show tmp = shows[i];
             int j = i - 1;
             
             while (j >= 0) {
                 comparacoes++;
                 int comp = shows[j].getType().compareTo(tmp.getType());
                 
                 // Se types forem iguais, compara por title
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
             fw.write("matricula: 1404192\t");
             fw.write("comparacoes: " + comparacoes + "\t");
             fw.write("movimentacoes: " + movimentacoes + "\t");
             fw.write("tempo execucao: " + ((fim - inicio) / 1000.0) + "s\t");
         } catch (IOException e) {
             System.out.println("Erro ao gerar arquivo de log: " + e.getMessage());
         }
     }
 }
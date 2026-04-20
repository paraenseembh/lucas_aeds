import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Classe Show para representar os dados do Disney+
 */
class Show {
    private String show_id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private String date_added;
    private int release_year;
    private String rating;
    private String duration;
    private String[] listed_in;
    private String description;

    /**
     * Construtor padrão
     */
    public Show() {
        this.show_id = "";
        this.type = "";
        this.title = "";
        this.director = "";
        this.cast = new String[0];
        this.country = "";
        this.date_added = "";
        this.release_year = 0;
        this.rating = "";
        this.duration = "";
        this.listed_in = new String[0];
        this.description = "";
    }

    /**
     * Construtor com parâmetros
     */
    public Show(String show_id, String type, String title, String director, String[] cast,
                String country, String date_added, int release_year, String rating,
                String duration, String[] listed_in, String description) {
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
    }

    // Getters e Setters
    public String getShowId() { return show_id; }
    public void setShowId(String show_id) { this.show_id = show_id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String[] getCast() { return cast; }
    public void setCast(String[] cast) { this.cast = cast; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDateAdded() { return date_added; }
    public void setDateAdded(String date_added) { this.date_added = date_added; }

    public int getReleaseYear() { return release_year; }
    public void setReleaseYear(int release_year) { this.release_year = release_year; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String[] getListedIn() { return listed_in; }
    public void setListedIn(String[] listed_in) { this.listed_in = listed_in; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Método clone que retorna uma cópia do objeto
     * @return uma cópia do objeto Show
     */
    public Show clone() {
        Show clone = new Show();
        clone.show_id = this.show_id;
        clone.type = this.type;
        clone.title = this.title;
        clone.director = this.director;
        clone.cast = this.cast.clone();
        clone.country = this.country;
        clone.date_added = this.date_added;
        clone.release_year = this.release_year;
        clone.rating = this.rating;
        clone.duration = this.duration;
        clone.listed_in = this.listed_in.clone();
        clone.description = this.description;
        return clone;
    }

    /**
     * Método para ler um registro do arquivo CSV pelo ID
     * @param showId ID do show a ser lido
     */
    public void ler(String showId) {
        try {
            // Abre o arquivo
            File file = new File("/tmp/disneyplus.csv");
            Scanner scanner = new Scanner(file, "UTF-8");
            
            // Pula a linha de cabeçalho
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            // Busca o show pelo ID
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] campos = extrairCampos(linha);
                
                if (campos[0].equals(showId)) {
                    this.show_id = campos[0];
                    this.type = isVazio(campos[1]) ? "NaN" : campos[1];
                    this.title = isVazio(campos[2]) ? "NaN" : campos[2];
                    this.director = isVazio(campos[3]) ? "NaN" : campos[3];
                    
                    // Processa o elenco
                    if (isVazio(campos[4])) {
                        this.cast = new String[0];
                    } else {
                        this.cast = dividirEOrdenar(campos[4]);
                    }
                    
                    this.country = isVazio(campos[5]) ? "NaN" : campos[5];
                    this.date_added = isVazio(campos[6]) ? "NaN" : campos[6];
                    
                    if (isVazio(campos[7])) {
                        this.release_year = 0;
                    } else {
                        try {
                            this.release_year = Integer.parseInt(campos[7]);
                        } catch (NumberFormatException e) {
                            this.release_year = 0;
                        }
                    }
                    
                    this.rating = isVazio(campos[8]) ? "NaN" : campos[8];
                    this.duration = isVazio(campos[9]) ? "NaN" : campos[9];
                    
                    // Processa as categorias
                    if (isVazio(campos[10])) {
                        this.listed_in = new String[0];
                    } else {
                        this.listed_in = dividirEOrdenar(campos[10]);
                    }
                    
                    this.description = campos.length > 11 && !isVazio(campos[11]) ? campos[11] : "NaN";
                    break;
                }
            }
            
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    /**
     * Verifica se uma string está vazia
     */
    private boolean isVazio(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * Divide uma string por vírgulas e ordena os itens
     */
    private String[] dividirEOrdenar(String texto) {
        // Divide a string por vírgulas
        String[] partes = texto.split(", ");
        
        // Remove espaços extras
        for (int i = 0; i < partes.length; i++) {
            partes[i] = partes[i].trim();
        }
        
        // Ordenação simples (Insertion Sort)
        for (int i = 1; i < partes.length; i++) {
            String tmp = partes[i];
            int j = i - 1;
            
            while (j >= 0 && compararStrings(partes[j], tmp) > 0) {
                partes[j + 1] = partes[j];
                j--;
            }
            
            partes[j + 1] = tmp;
        }
        
        return partes;
    }

    /**
     * Compara duas strings alfabeticamente
     */
    private int compararStrings(String s1, String s2) {
        int minLength = s1.length() < s2.length() ? s1.length() : s2.length();
        
        for (int i = 0; i < minLength; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                return s1.charAt(i) - s2.charAt(i);
            }
        }
        
        return s1.length() - s2.length();
    }

    /**
     * Extrai os campos de uma linha do CSV
     */
    private String[] extrairCampos(String linha) {
        String[] campos = new String[12];
        StringBuilder campo = new StringBuilder();
        boolean dentroDeAspas = false;
        int indice = 0;
        
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            
            if (c == '"') {
                dentroDeAspas = !dentroDeAspas;
            } else if (c == ',' && !dentroDeAspas) {
                campos[indice++] = campo.toString();
                campo = new StringBuilder();
                
                // Assegura que não ultrapassamos o limite de campos
                if (indice >= campos.length) {
                    break;
                }
            } else {
                campo.append(c);
            }
        }
        
        // Adiciona o último campo
        if (indice < campos.length) {
            campos[indice] = campo.toString();
        }
        
        return campos;
    }

    /**
     * Método para imprimir o registro no formato especificado
     */
    public void imprimir() {
        System.out.print("=> " + this.show_id + " ## " + this.title + " ## " + this.type + " ## " + this.director + " ## [");
        
        // Imprime o elenco
        for (int i = 0; i < this.cast.length; i++) {
            System.out.print(this.cast[i]);
            if (i < this.cast.length - 1) {
                System.out.print(", ");
            }
        }
        
        System.out.print("] ## " + this.country + " ## " + this.date_added + " ## " + this.release_year + " ## " + this.rating + " ## " + this.duration + " ## [");
        
        // Imprime as categorias
        for (int i = 0; i < this.listed_in.length; i++) {
            System.out.print(this.listed_in[i]);
            if (i < this.listed_in.length - 1) {
                System.out.print(", ");
            }
        }
        
        System.out.println("]");
    }
}

/**
 * Classe principal para realizar a pesquisa sequencial
 */
public class TP02Q03 {
    /**
     * Método para realizar pesquisa sequencial
     * @param shows array de shows
     * @param n tamanho do array
     * @param titulo título a ser pesquisado
     * @return true se encontrar, false caso contrário
     */
    public static boolean pesquisaSequencial(Show[] shows, int n, String titulo, int[] numComp) {
        boolean encontrado = false;
        
        for (int i = 0; i < n; i++) {
            numComp[0]++; // Incrementa o contador de comparações
            if (shows[i].getTitle().equals(titulo)) {
                encontrado = true;
                break;
            }
        }
        
        return encontrado;
    }
    
    public static void main(String[] args) {
        Show[] shows = new Show[1368]; // Tamanho máximo do arquivo
        int n = 0;
        
        // Leitura dos registros
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        
        while (!id.equals("FIM")) {
            shows[n] = new Show();
            shows[n].ler(id);
            n++;
            id = scanner.nextLine();
        }
        
        // Arquivo de log
        long inicio = System.currentTimeMillis();
        int[] numComparacoes = {0}; // Array para permitir passagem por referência
        
        // Leitura dos títulos a serem pesquisados
        String titulo = scanner.nextLine();
        while (!titulo.equals("FIM")) {
            boolean encontrado = pesquisaSequencial(shows, n, titulo, numComparacoes);
            System.out.println(encontrado ? "SIM" : "NAO");
            titulo = scanner.nextLine();
        }
        
        long fim = System.currentTimeMillis();
        
        // Criação do arquivo de log, com numero de matricula
        try {
            FileWriter fw = new FileWriter("matricula_sequencial.txt");
            fw.write("1404192\t" + (fim - inicio) / 1000.0 + "\t" + numComparacoes[0]);
            fw.close();
        } catch (Exception e) {
            System.out.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
        
        scanner.close();
    }
}
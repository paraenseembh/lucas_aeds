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
     * Método para ler um registro do arquivo CSV
     * @param showId id do show a ser lido
     */
    public void ler(String showId) {
        try {
            File file = new File("disneyplus.csv");
            Scanner scanner = new Scanner(file, "UTF-8");
            
            // Descarta a primeira linha (cabeçalho)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            // Busca pelo show_id
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                // Trata valores com vírgulas dentro de aspas
                String[] valores = tratarLinha(linha);
                
                if (valores[0].equals(showId)) {
                    this.show_id = valores[0];
                    this.type = valores[1].isEmpty() ? "NaN" : valores[1];
                    this.title = valores[2].isEmpty() ? "NaN" : valores[2];
                    this.director = valores[3].isEmpty() ? "NaN" : valores[3];
                    
                    // Trata o elenco (cast)
                    if (valores[4].isEmpty()) {
                        this.cast = new String[0];
                    } else {
                        String[] castTemp = valores[4].split(", ");
                        this.cast = new String[castTemp.length];
                        for (int i = 0; i < castTemp.length; i++) {
                            this.cast[i] = castTemp[i].trim();
                        }
                        // Ordena o cast
                        java.util.Arrays.sort(this.cast);
                    }
                    
                    this.country = valores[5].isEmpty() ? "NaN" : valores[5];
                    this.date_added = valores[6].isEmpty() ? "NaN" : valores[6];
                    
                    try {
                        this.release_year = valores[7].isEmpty() ? 0 : Integer.parseInt(valores[7]);
                    } catch (NumberFormatException e) {
                        this.release_year = 0;
                    }
                    
                    this.rating = valores[8].isEmpty() ? "NaN" : valores[8];
                    this.duration = valores[9].isEmpty() ? "NaN" : valores[9];
                    
                    // Trata as categorias (listed_in)
                    if (valores[10].isEmpty()) {
                        this.listed_in = new String[0];
                    } else {
                        String[] listedTemp = valores[10].split(", ");
                        this.listed_in = new String[listedTemp.length];
                        for (int i = 0; i < listedTemp.length; i++) {
                            this.listed_in[i] = listedTemp[i].trim();
                        }
                        // Ordena o listed_in
                        java.util.Arrays.sort(this.listed_in);
                    }
                    
                    this.description = valores.length > 11 && !valores[11].isEmpty() ? valores[11] : "NaN";
                    break;
                }
            }
            
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para tratar linhas do CSV com vírgulas dentro de aspas
     */
    private String[] tratarLinha(String linha) {
        java.util.ArrayList<String> tokens = new java.util.ArrayList<>();
        boolean dentroDeAspas = false;
        StringBuilder token = new StringBuilder();
        
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            
            if (c == '\"') {
                dentroDeAspas = !dentroDeAspas;
            } else if (c == ',' && !dentroDeAspas) {
                tokens.add(token.toString());
                token = new StringBuilder();
            } else {
                token.append(c);
            }
        }
        
        tokens.add(token.toString());
        
        return tokens.toArray(new String[0]);
    }

    /**
     * Método para imprimir o registro no formato especificado
     */
    public void imprimir() {
        System.out.print("=> " + this.show_id + " ## " + this.type + " ## " + this.title + " ## " + this.director + " ## [");
        
        // Imprime o cast
        for (int i = 0; i < this.cast.length; i++) {
            System.out.print(this.cast[i]);
            if (i < this.cast.length - 1) {
                System.out.print(", ");
            }
        }
        
        System.out.print("] ## " + this.country + " ## " + this.date_added + " ## " + this.release_year + " ## " + this.rating + " ## " + this.duration + " ## [");
        
        // Imprime as categorias (listed_in)
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
 * Classe para implementar a pesquisa sequencial
 */
public class TP02Q03 {
    /**
     * Método para realizar pesquisa sequencial
     * @param shows array de shows
     * @param n tamanho do array
     * @param titulo título a ser pesquisado
     * @return true se encontrar, false caso contrário
     */
    public static boolean pesquisaSequencial(Show[] shows, int n, String titulo) {
        for (int i = 0; i < n; i++) {
            if (shows[i].getTitle().equals(titulo)) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        Show[] shows = new Show[1000]; // Tamanho arbitrário
        int n = 0;
        String id;
        
        // Leitura dos registros
        Scanner scanner = new Scanner(System.in);
        id = scanner.nextLine();
        while (!id.equals("FIM")) {
            shows[n] = new Show();
            shows[n].ler(id);
            n++;
            id = scanner.nextLine();
        }
        
        // Arquivo de log
        long inicio = System.currentTimeMillis();
        int numComparacoes = 0;
        
        // Leitura dos títulos a serem pesquisados
        String titulo = scanner.nextLine();
        while (!titulo.equals("FIM")) {
            boolean encontrado = pesquisaSequencial(shows, n, titulo);
            System.out.println(encontrado ? "SIM" : "NAO");
            titulo = scanner.nextLine();
            numComparacoes++;
        }
        
        long fim = System.currentTimeMillis();
        
        // Criação do arquivo de log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("matricula_sequencial.txt");
            fw.write("12345\t" + (fim - inicio) / 1000.0 + "\t" + numComparacoes);
            fw.close();
        } catch (Exception e) {
            System.out.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
        
        scanner.close();
    }
}
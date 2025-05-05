/**
 * Ordenação por Seleção para Shows do Disney+
 * @author Lucas Nascimento
 * @version 1.0 MM/YYYY
 */

import java.io.*;
import java.text.*;
import java.util.*;

class Show {
    private String show_id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private Date dateAdded;
    private int releaseYear;
    private String rating;
    private String duration;
    private String[] listedIn;
    
    // Construtor vazio
    public Show() {
        this.show_id = "";
        this.type = "";
        this.title = "";
        this.director = "";
        this.cast = new String[0];
        this.country = "";
        this.dateAdded = null;
        this.releaseYear = 0;
        this.rating = "";
        this.duration = "";
        this.listedIn = new String[0];
    }
    
    // Construtor com parâmetros
    public Show(String show_id, String type, String title, String director, String[] cast,
                String country, Date dateAdded, int releaseYear, String rating, 
                String duration, String[] listedIn) {
        this.show_id = show_id;
        this.type = type;
        this.title = title;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.dateAdded = dateAdded;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.duration = duration;
        this.listedIn = listedIn;
    }
    
    // Métodos getters e setters
    public String getShowId() { return this.show_id; }
    public void setShowId(String show_id) { this.show_id = show_id; }
    
    public String getType() { return this.type; }
    public void setType(String type) { this.type = type; }
    
    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDirector() { return this.director; }
    public void setDirector(String director) { this.director = director; }
    
    public String[] getCast() { return this.cast; }
    public void setCast(String[] cast) { this.cast = cast; }
    
    public String getCountry() { return this.country; }
    public void setCountry(String country) { this.country = country; }
    
    public Date getDateAdded() { return this.dateAdded; }
    public void setDateAdded(Date dateAdded) { this.dateAdded = dateAdded; }
    
    public int getReleaseYear() { return this.releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    
    public String getRating() { return this.rating; }
    public void setRating(String rating) { this.rating = rating; }
    
    public String getDuration() { return this.duration; }
    public void setDuration(String duration) { this.duration = duration; }
    
    public String[] getListedIn() { return this.listedIn; }
    public void setListedIn(String[] listedIn) { this.listedIn = listedIn; }
    
    // Método clone
    public Show clone() {
        Show clone = new Show();
        clone.show_id = this.show_id;
        clone.type = this.type;
        clone.title = this.title;
        clone.director = this.director;
        clone.cast = this.cast.clone();
        clone.country = this.country;
        clone.dateAdded = this.dateAdded != null ? (Date)this.dateAdded.clone() : null;
        clone.releaseYear = this.releaseYear;
        clone.rating = this.rating;
        clone.duration = this.duration;
        clone.listedIn = this.listedIn.clone();
        return clone;
    }
    
    // Método para ler um show a partir do ID
    public void ler(String id) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/tmp/disneyplus.csv"));
            String line = br.readLine(); // Pula a linha de cabeçalho
            
            while ((line = br.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                if (fields[0].equals(id)) {
                    this.show_id = fields[0];
                    this.type = fields[1];
                    this.title = fields[2];
                    this.director = fields[3].isEmpty() ? "NaN" : fields[3];
                    
                    // Tratamento do elenco
                    if (fields[4].isEmpty()) {
                        this.cast = new String[0];
                    } else {
                        String[] castArray = fields[4].split(", ");
                        Arrays.sort(castArray);
                        this.cast = castArray;
                    }
                    
                    this.country = fields[5].isEmpty() ? "NaN" : fields[5];
                    
                    // Tratamento da data
                    if (fields[6].isEmpty()) {
                        this.dateAdded = null;
                    } else {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                            this.dateAdded = sdf.parse(fields[6]);
                        } catch (ParseException e) {
                            this.dateAdded = null;
                        }
                    }
                    
                    this.releaseYear = Integer.parseInt(fields[7]);
                    this.rating = fields[8].isEmpty() ? "NaN" : fields[8];
                    this.duration = fields[9].isEmpty() ? "NaN" : fields[9];
                    
                    // Tratamento das categorias
                    if (fields[10].isEmpty()) {
                        this.listedIn = new String[0];
                    } else {
                        String[] listedInArray = fields[10].split(", ");
                        Arrays.sort(listedInArray);
                        this.listedIn = listedInArray;
                    }
                    
                    break;
                }
            }
            
            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
    
    // Método para fazer o parsing correto de uma linha CSV
    private String[] parseCsvLine(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(sb.toString().trim());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        
        fields.add(sb.toString().trim());
        return fields.toArray(new String[0]);
    }
    
    // Método para imprimir um show no formato especificado
    public void imprimir() {
        System.out.print("=> " + this.show_id + " ## " + this.type + " ## " + this.title + " ## " + this.director + " ## [");
        
        // Imprime o elenco
        if (this.cast.length > 0) {
            for (int i = 0; i < this.cast.length; i++) {
                System.out.print(this.cast[i]);
                if (i < this.cast.length - 1) {
                    System.out.print(", ");
                }
            }
        } else {
            System.out.print("NaN");
        }
        
        System.out.print("] ## " + this.country + " ## ");
        
        // Imprime a data adicionada
        if (this.dateAdded != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            System.out.print(sdf.format(this.dateAdded));
        } else {
            System.out.print("NaN");
        }
        
        System.out.print(" ## " + this.releaseYear + " ## " + this.rating + " ## " + this.duration + " ## [");
        
        // Imprime as categorias
        if (this.listedIn.length > 0) {
            for (int i = 0; i < this.listedIn.length; i++) {
                System.out.print(this.listedIn[i]);
                if (i < this.listedIn.length - 1) {
                    System.out.print(", ");
                }
            }
        } else {
            System.out.print("NaN");
        }
        
        System.out.println("] ##");
    }
}

public class TP02Q05 {
    
    /**
     * Método de ordenação por seleção
     * @param shows Array de shows a ser ordenado
     * @return Um array com número de comparações e movimentações
     */
    public static int[] selectionSort(Show[] shows, int n) {
        int[] stats = new int[2]; // [0] = comparações, [1] = movimentações
        
        for (int i = 0; i < n - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < n; j++) {
                stats[0]++; // Incrementa o número de comparações
                if (shows[j].getTitle().compareTo(shows[menor].getTitle()) < 0) {
                    menor = j;
                }
            }
            
            // Troca os elementos se necessário
            if (menor != i) {
                Show temp = shows[i];
                shows[i] = shows[menor];
                shows[menor] = temp;
                stats[1] += 3; // 3 movimentações por troca
            }
        }
        
        return stats;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Show> showList = new ArrayList<>();
        
        // Lê os IDs dos shows até encontrar "FIM"
        String id = scanner.nextLine();
        while (!id.equals("FIM")) {
            Show show = new Show();
            show.ler(id);
            showList.add(show);
            id = scanner.nextLine();
        }
        
        // Converte a lista para array para usar na ordenação
        Show[] shows = showList.toArray(new Show[0]);
        int n = shows.length;
        
        // Aplicando o algoritmo de ordenação por seleção e medindo o tempo
        long startTime = System.currentTimeMillis();
        int[] stats = selectionSort(shows, n);
        long endTime = System.currentTimeMillis();
        
        // Mostra os shows ordenados
        for (int i = 0; i < n; i++) {
            shows[i].imprimir();
        }
        
        // Criar arquivo de log
        try {
            FileWriter fw = new FileWriter("1404192_selecao.txt");
            fw.write("1404192\t" + stats[0] + "\t" + stats[1] + "\t" + (endTime - startTime) / 1000.0);
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
        
        scanner.close();
    }
}
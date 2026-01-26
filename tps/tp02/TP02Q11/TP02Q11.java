import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

class Show {
    private String show_id, type, title, director, cast,
            country, date_added, release_year, rating, duration, listed_in, description;
    private String elenco[], listados[];

    public Show() {
        return;
    }

    String getShowId(){
        return show_id;
    }

    String getTitle(){
        return title;
    }

    String getDirector(){
        return director;
    }

    String getReleaseYear(){
        return release_year;
    }

    private static String[] separaStrings(String linha) {
        String[] s = linha.split(",");

        for (int k = 0; k < s.length - 1; k++) {
            int menor = k;
            for (int l = k + 1; l < s.length; l++) {
                if (s[l].trim().compareTo(s[menor].trim()) < 0) {
                    menor = l;
                }
            }
            String aux = s[k];
            s[k] = s[menor];
            s[menor] = aux;
        }

        return s;
    }

    public static Show ler(String linha) {
        Show show = new Show();
        int virgula = 0, caracteres = 0, tam = linha.length();
        boolean aspas = false;
        String separadores[] = new String[12];
        separadores[0] = "";

        for (int i = 0; i < tam; i++) {
            if (!aspas) {
                if (linha.charAt(i) == '"') {
                    aspas = true;
                } else if (linha.charAt(i) == ',') {
                    if (caracteres == 0) {
                        separadores[virgula] = "NaN";
                    }
                    virgula++;
                    separadores[virgula] = "";
                    caracteres = 0;
                } else {
                    separadores[virgula] += linha.charAt(i);
                    caracteres++;
                }
            } else {
                if (linha.charAt(i) == '"') {
                    aspas = false;
                } else {
                    separadores[virgula] += linha.charAt(i);
                    caracteres++;
                }
            }
        }

        show.show_id = separadores[0];
        show.type = separadores[1];
        show.title = separadores[2];
        show.director = separadores[3];
        show.cast = separadores[4];
        show.country = separadores[5];
        show.date_added = separadores[6];
        show.release_year = separadores[7];
        show.rating = separadores[8];
        show.duration = separadores[9];
        show.listed_in = separadores[10];
        show.description = separadores[11];

        show.elenco = separaStrings(show.cast);
        show.listados = separaStrings(show.listed_in);

        return show;
    }

    public void imprimir() {
        System.out.printf("=> %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## \r\n",
                this.show_id, this.title, this.type, this.director, Arrays.toString(this.elenco), this.country,
                this.date_added,
                this.release_year, this.rating, this.duration, Arrays.toString(this.listados));
    }

}

class CountingSortShow {

    private Show[] array;
    private int n;

    private int comparacoes = 0;
    private int movimentacoes = 0;

    public CountingSortShow(Show[] shows) {
        this.array = shows;
        this.n = shows.length;
    }

    public void sort() {
        // Etapa 1: ordenar por título (desempate)
        insertionSortByTitle();

        // Etapa 2: counting sort estável por ano de lançamento
        countingSortByReleaseYear();
    }

    // Ordenação estável por título (insertion sort)
    private void insertionSortByTitle() {
        for (int i = 1; i < n; i++) {
            Show tmp = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].getTitle().compareTo(tmp.getTitle()) > 0) {
                comparacoes++;
                array[j + 1] = array[j];
                movimentacoes++;
                j--;
            }
            array[j + 1] = tmp;
            movimentacoes++;
        }
    }

    // Counting Sort por release year (com desempate preservado por estabilidade)
    private void countingSortByReleaseYear() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            int year = Integer.parseInt(array[i].getReleaseYear());
            if (year < min) min = year;
            if (year > max) max = year;
            comparacoes += 2;
        }

        int range = max - min + 1;
        int[] count = new int[range];
        Show[] output = new Show[n];

        // Contar frequência dos anos
        for (int i = 0; i < n; i++) {
            int year = Integer.parseInt(array[i].getReleaseYear());
            count[year - min]++;
            movimentacoes++;
        }

        // Acumular os índices
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
            movimentacoes++;
        }

        // Construir array de saída (de trás pra frente para manter estabilidade)
        for (int i = n - 1; i >= 0; i--) {
            int year = Integer.parseInt(array[i].getReleaseYear());
            int pos = count[year - min] - 1;
            output[pos] = array[i];
            count[year - min]--;
            movimentacoes++;
        }

        // Copiar para o array original
        for (int i = 0; i < n; i++) {
            array[i] = output[i];
        }
    }

    public Show[] getSortedArray() {
        return array;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public int getMovimentacoes() {
        return movimentacoes;
    }
}

public class TP02Q11 {
    public static void main(String args[]) throws FileNotFoundException, IOException {
        File file = new File("../tmp/disneyplus.csv");
        Scanner sc = new Scanner(file);
        String linha = new String();
        int i = 0;
        Show[] shows = new Show[1368];
        sc.nextLine();

        while (sc.hasNext()) {
            linha = sc.nextLine();
            shows[i] = Show.ler(linha);
            i++;
        }

        sc.close();

        Show[] meusShows = new Show[300];
        int k = 0;
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();

        while(!s.equals("FIM")){
            for(int j=0; j<1368; j++){
                if(s.equals(shows[j].getShowId())){
                    meusShows[k] = shows[j];
                    k++;
                    j = 1368;
                }
            }
            s = scanner.nextLine();
        }

        long inicio = System.nanoTime();
        CountingSortShow count = new CountingSortShow(meusShows);
        long fim = System.nanoTime();
        count.sort();
        meusShows = count.getSortedArray();

        for(int j=0; j<meusShows.length; j++){
            meusShows[j].imprimir();;
        }

        long tempoExecucao = fim - inicio;
        FileWriter fw = new FileWriter("matricula_countingsort.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 1404192\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %d\n", count.getComparacoes(), count.getMovimentacoes(), tempoExecucao); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }
}



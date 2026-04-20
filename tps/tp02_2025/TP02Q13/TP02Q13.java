package tps.tp02.TP02Q13;
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

    String getDuration(){
        return duration;
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

class MergeSortShow {

    private Show[] array;
    private int comparacoes = 0;
    private int movimentacoes = 0;

    public MergeSortShow(Show[] shows) {
        this.array = shows;
    }

    public void sort() {
        mergeSort(0, array.length - 1);
    }

    private void mergeSort(int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSort(esq, meio);
            mergeSort(meio + 1, dir);
            merge(esq, meio, dir);
        }
    }

    private void merge(int esq, int meio, int dir) {
        int n1 = meio - esq + 1;
        int n2 = dir - meio;

        Show[] L = new Show[n1];
        Show[] R = new Show[n2];

        for (int i = 0; i < n1; i++) {
            movimentacoes++;
            L[i] = array[esq + i];
        }
        for (int j = 0; j < n2; j++) {
            movimentacoes++;
            R[j] = array[meio + 1 + j];
        }

        int i = 0, j = 0, k = esq;

        while (i < n1 && j < n2) {
            comparacoes++;
            if (compareShows(L[i], R[j]) <= 0) {
                array[k++] = L[i++];
            } else {
                array[k++] = R[j++];
            }
            movimentacoes++;
        }

        while (i < n1) {
            array[k++] = L[i++];
            movimentacoes++;
        }

        while (j < n2) {
            array[k++] = R[j++];
            movimentacoes++;
        }
    }

    // ✅ Comparação com desempate e duração como string
    private int compareShows(Show a, Show b) {
        String durA = parseDurationString(a.getDuration());
        String durB = parseDurationString(b.getDuration());

        comparacoes++;
        int cmp = durA.compareTo(durB);
        if (cmp != 0) return cmp;

        comparacoes++;
        return a.getTitle().compareTo(b.getTitle());
    }

    // ✅ Extrai apenas os números da string, mas retorna como string
    private String parseDurationString(String dur) {
        if (dur == null) return "";
        return dur.replaceAll("[^0-9]", "");
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

public class TP02Q13 {
    public static void main(String args[]) throws FileNotFoundException, IOException {
        File file = new File("/tmp/disneyplus.csv");
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
        MergeSortShow merge = new MergeSortShow(meusShows);
        long fim = System.nanoTime();
        merge.sort();
        meusShows = merge.getSortedArray();

        for(int j=0; j<meusShows.length; j++){
            meusShows[j].imprimir();;
        }

        long tempoExecucao = fim - inicio;
        FileWriter fw = new FileWriter("matricula_mergesort.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 1404192\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %d\n", merge.getComparacoes(), merge.getMovimentacoes(), tempoExecucao); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }
}



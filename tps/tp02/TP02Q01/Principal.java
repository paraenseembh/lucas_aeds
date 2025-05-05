import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

class Show {
    private String show_id, type, title, director, cast,
            country, date_added, release_year, rating, duration, listed_in, description;
    private String[] elenco, listados;

    public Show() {
        // Construtor vazio
    }

    /**
     * Ordena e retorna um array de strings separadas por vírgula
     */
    private static String[] separaStrings(String linha) {
        String[] s = linha.split(",");

        // Ordenação por seleção
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
    
    /**
     * Lê uma linha do arquivo CSV e cria um objeto Show
     */
    public static Show ler(String linha) {
        Show show = new Show();
        int virgula = 0, caracteres = 0, tam = linha.length();
        boolean aspas = false;
        String[] separadores = new String[12];
        separadores[0] = "";

        // Processamento manual do CSV para lidar com texto entre aspas
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

        // Atribuição dos campos
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

        // Processamento de arrays
        show.elenco = separaStrings(show.cast);
        show.listados = separaStrings(show.listed_in);

        return show;
    }

    /**
     * Imprime os dados do show no formato especificado
     */
    public void imprimir() {
        System.out.printf("=> %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## \r\n",
                show_id, title, type, director, Arrays.toString(elenco), country,
                date_added, release_year, rating, duration, Arrays.toString(listados));
    }
}

public class Principal {
    public static void main(String[] args) throws FileNotFoundException {
        // Leitura do arquivo CSV
        File file = new File("/tmp/disneyplus.csv");
        Scanner sc = new Scanner(file);
        String linha;
        int i = 0;
        Show[] shows = new Show[1368];
        sc.nextLine(); // Pula o cabeçalho

        // Carrega todos os shows do arquivo
        while (sc.hasNext()) {
            linha = sc.nextLine();
            shows[i] = Show.ler(linha);
            i++;
        }
        sc.close();

        // Processamento das entradas do usuário
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        while (!s.equals("FIM")) {
            int pos = Integer.parseInt(s.substring(1));
            shows[pos - 1].imprimir();
            s = scanner.nextLine();
        }
        scanner.close();
    }
}
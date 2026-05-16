import java.util.*;
import java.io.*;

class Data {
    private int ano, mes, dia;
    public Data(int ano, int mes, int dia) { this.ano=ano; this.mes=mes; this.dia=dia; }
    public static Data parseData(String s) {
        String[] p = s.split("-");
        return new Data(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
    }
    public String formatar() { return String.format("%02d/%02d/%04d", dia, mes, ano); }
}

class Hora {
    private int hora, minuto;
    public Hora(int hora, int minuto) { this.hora=hora; this.minuto=minuto; }
    public static Hora parseHora(String s) {
        String[] p = s.split(":");
        return new Hora(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
    }
    public String formatar() { return String.format("%02d:%02d", hora, minuto); }
}

class Restaurante {
    private int id;
    private String nome, cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixaPreco;
    private Hora horarioAbertura, horarioFechamento;
    private Data dataAbertura;
    private boolean aberto;

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getAvaliacao() { return avaliacao; }

    public static Restaurante parseRestaurante(String s) {
        String[] p = s.split(",");
        Restaurante r = new Restaurante();
        r.id = Integer.parseInt(p[0]);
        r.nome = p[1];
        r.cidade = p[2];
        r.capacidade = Integer.parseInt(p[3]);
        r.avaliacao = Double.parseDouble(p[4]);
        r.tiposCozinha = p[5].split(";");
        r.faixaPreco = p[6].length();
        String[] h = p[7].split("-");
        r.horarioAbertura = Hora.parseHora(h[0]);
        r.horarioFechamento = Hora.parseHora(h[1]);
        r.dataAbertura = Data.parseData(p[8]);
        r.aberto = Boolean.parseBoolean(p[9].trim());
        return r;
    }

    public String formatar() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(id).append(" ## ").append(nome).append(" ## ");
        sb.append(cidade).append(" ## ").append(capacidade).append(" ## ");
        sb.append(String.format(java.util.Locale.US, "%.1f", avaliacao)).append(" ## [");
        for (int i = 0; i < tiposCozinha.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(tiposCozinha[i]);
        }
        sb.append("] ## ");
        for (int i = 0; i < faixaPreco; i++) sb.append("$");
        sb.append(" ## ").append(horarioAbertura.formatar()).append("-").append(horarioFechamento.formatar());
        sb.append(" ## ").append(dataAbertura.formatar()).append(" ## ").append(aberto).append("]");
        return sb.toString();
    }
}

public class Q3 {
    static long comparacoes = 0, movimentacoes = 0;
    static int K;

    static int compara(Restaurante a, Restaurante b) {
        if (a.getAvaliacao() < b.getAvaliacao()) return -1;
        if (a.getAvaliacao() > b.getAvaliacao()) return 1;
        return a.getNome().compareTo(b.getNome());
    }

    static int partition(Restaurante[] arr, int left, int right) {
        Restaurante pivot = arr[(left + right) / 2];
        int i = left - 1;
        int j = right + 1;
        while (true) {
            do { i++; comparacoes++; } while (compara(arr[i], pivot) < 0);
            do { j--; comparacoes++; } while (compara(arr[j], pivot) > 0);
            if (i >= j) return j;
            Restaurante tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            movimentacoes += 3;
        }
    }

    static void quicksortParcial(Restaurante[] arr, int left, int right) {
        if (left < right) {
            int p = partition(arr, left, right);
            quicksortParcial(arr, left, p);
            if (p + 1 < K) quicksortParcial(arr, p + 1, right);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader csv = new BufferedReader(new FileReader("/tmp/restaurantes.csv"));
        csv.readLine();
        Map<Integer, Restaurante> mapa = new HashMap<>();
        String line;
        while ((line = csv.readLine()) != null)
            if (!line.trim().isEmpty()) {
                Restaurante r = Restaurante.parseRestaurante(line);
                mapa.put(r.getId(), r);
            }
        csv.close();

        Scanner sc = new Scanner(System.in);
        List<Restaurante> lista = new ArrayList<>();
        int id;
        while ((id = sc.nextInt()) != -1)
            if (mapa.containsKey(id)) lista.add(mapa.get(id));

        Restaurante[] arr = lista.toArray(new Restaurante[0]);
        int n = arr.length;
        K = n / 5;

        long ini = System.currentTimeMillis();
        quicksortParcial(arr, 0, n - 1);
        long fim = System.currentTimeMillis();

        for (Restaurante r : arr) System.out.println(r.formatar());

        PrintWriter log = new PrintWriter("797798_quicksort.txt");
        log.printf("797798\t%d\t%d\t%.2f%n", comparacoes, movimentacoes, (fim - ini) / 1000.0);
        log.close();
    }
}

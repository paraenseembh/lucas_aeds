import java.io.*;
import java.util.*;

public class Q4 {

    static final String MATRICULA = "868511";
    static final int TAM_TAB = 83;

    static class Restaurante {
        int id;
        String nome, cidade;
        int capacidade;
        double avaliacao;
        String[] tiposCozinha;
        int faixaPreco;
        int aberturaH, aberturaM, fechamentoH, fechamentoM;
        int dataAno, dataMes, dataDia;
        boolean aberto;

        String formatar() {
            StringBuilder sb = new StringBuilder();
            sb.append('[').append(id).append(" ## ").append(nome).append(" ## ")
              .append(cidade).append(" ## ").append(capacidade).append(" ## ")
              .append(String.format("%.1f", avaliacao)).append(" ## [");
            for (int i = 0; i < tiposCozinha.length; i++) {
                if (i > 0) sb.append(',');
                sb.append(tiposCozinha[i]);
            }
            sb.append("] ## ");
            for (int i = 0; i < faixaPreco; i++) sb.append('$');
            sb.append(String.format(" ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]",
                    aberturaH, aberturaM, fechamentoH, fechamentoM,
                    dataDia, dataMes, dataAno, aberto ? "true" : "false"));
            return sb.toString();
        }
    }

    static Restaurante parseRestaurante(String line) {
        line = line.stripTrailing();
        String[] f = line.split(",", -1);
        Restaurante r = new Restaurante();
        r.id = Integer.parseInt(f[0]);
        r.nome = f[1];
        r.cidade = f[2];
        r.capacidade = Integer.parseInt(f[3]);
        r.avaliacao = Double.parseDouble(f[4]);
        r.tiposCozinha = f[5].split(";");
        r.faixaPreco = f[6].length();
        String[] parts = f[7].split("-");
        String[] ab = parts[0].split(":");
        String[] fe = parts[1].split(":");
        r.aberturaH = Integer.parseInt(ab[0]);
        r.aberturaM = Integer.parseInt(ab[1]);
        r.fechamentoH = Integer.parseInt(fe[0]);
        r.fechamentoM = Integer.parseInt(fe[1]);
        String[] dt = f[8].split("-");
        r.dataAno = Integer.parseInt(dt[0]);
        r.dataMes = Integer.parseInt(dt[1]);
        r.dataDia = Integer.parseInt(dt[2]);
        r.aberto = f[9].trim().equals("true");
        return r;
    }

    static Map<Integer, Restaurante> carregarCSV() throws IOException {
        Map<Integer, Restaurante> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("/tmp/restaurantes.csv"));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null)
            if (!line.isBlank()) {
                Restaurante r = parseRestaurante(line);
                map.put(r.id, r);
            }
        br.close();
        return map;
    }

    static int ascii(String nome) {
        int s = 0;
        for (char c : nome.toCharArray()) s += c;
        return s;
    }

    static int h1(String nome) { return ascii(nome) % TAM_TAB; }
    static int h2(String nome) { return (ascii(nome) + 1) % TAM_TAB; }

    static Restaurante[] tabela = new Restaurante[TAM_TAB];

    static void inserir(Restaurante r) {
        int p1 = h1(r.nome);
        if (tabela[p1] == null) { tabela[p1] = r; return; }
        int p2 = h2(r.nome);
        if (tabela[p2] == null) { tabela[p2] = r; return; }
        System.out.println(r.nome);
    }

    static void buscar(String nome) {
        int p1 = h1(nome);
        if (tabela[p1] != null && tabela[p1].nome.equals(nome)) {
            System.out.println(p1 + " " + tabela[p1].formatar());
            return;
        }
        int p2 = h2(nome);
        if (tabela[p2] != null && tabela[p2].nome.equals(nome)) {
            System.out.println(p2 + " " + tabela[p2].formatar());
            return;
        }
        System.out.println(-1);
    }

    public static void main(String[] args) throws IOException {
        Map<Integer, Restaurante> csv = carregarCSV();
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextInt()) {
            int id = sc.nextInt();
            if (id == -1) break;
            Restaurante r = csv.get(id);
            if (r != null) inserir(r);
        }
        sc.nextLine();

        while (sc.hasNextLine()) {
            String query = sc.nextLine().trim();
            if (query.equals("FIM")) break;
            buscar(query);
        }

        PrintWriter log = new PrintWriter(new FileWriter(MATRICULA + "_hash_rehash.txt"));
        log.printf("%s\t0\t0.00%n", MATRICULA);
        log.close();
    }
}

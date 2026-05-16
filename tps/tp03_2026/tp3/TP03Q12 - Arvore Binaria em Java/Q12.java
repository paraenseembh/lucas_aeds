import java.io.*;
import java.util.*;

public class Q12 {

    static final String MATRICULA = "797798";

    // ── Restaurante ──────────────────────────────────────────────────────────

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
        // strip trailing whitespace
        line = line.stripTrailing();
        // split on comma but tipos_cozinha may contain semicolons - fields are comma-separated
        String[] f = line.split(",", -1);
        Restaurante r = new Restaurante();
        r.id = Integer.parseInt(f[0]);
        r.nome = f[1];
        r.cidade = f[2];
        r.capacidade = Integer.parseInt(f[3]);
        r.avaliacao = Double.parseDouble(f[4]);
        r.tiposCozinha = f[5].split(";");
        r.faixaPreco = f[6].length();
        // horario: HH:MM-HH:MM
        String[] parts = f[7].split("-");
        String[] ab = parts[0].split(":");
        String[] fe = parts[1].split(":");
        r.aberturaH = Integer.parseInt(ab[0]);
        r.aberturaM = Integer.parseInt(ab[1]);
        r.fechamentoH = Integer.parseInt(fe[0]);
        r.fechamentoM = Integer.parseInt(fe[1]);
        // data: YYYY-MM-DD
        String[] dt = f[8].split("-");
        r.dataAno = Integer.parseInt(dt[0]);
        r.dataMes = Integer.parseInt(dt[1]);
        r.dataDia = Integer.parseInt(dt[2]);
        r.aberto = f[9].trim().equals("true");
        return r;
    }

    // ── BST ──────────────────────────────────────────────────────────────────

    static class No {
        Restaurante dado;
        No esq, dir;
        No(Restaurante r) { dado = r; }
    }

    static No raiz = null;

    static void inserir(Restaurante r) {
        No novo = new No(r);
        if (raiz == null) { raiz = novo; return; }
        No atual = raiz;
        while (true) {
            int cmp = r.nome.compareTo(atual.dado.nome);
            if (cmp < 0) {
                if (atual.esq == null) { atual.esq = novo; return; }
                atual = atual.esq;
            } else {
                if (atual.dir == null) { atual.dir = novo; return; }
                atual = atual.dir;
            }
        }
    }

    static String buscar(String nome) {
        StringBuilder path = new StringBuilder("raiz");
        No atual = raiz;
        while (atual != null) {
            int cmp = nome.compareTo(atual.dado.nome);
            if (cmp == 0) { path.append(" SIM"); return path.toString(); }
            if (cmp < 0) { path.append(" esq"); atual = atual.esq; }
            else         { path.append(" dir"); atual = atual.dir; }
        }
        path.append(" NAO");
        return path.toString();
    }

    // ── In-order traversal ───────────────────────────────────────────────────

    static void inorder(No no, List<Restaurante> result) {
        if (no == null) return;
        inorder(no.esq, result);
        result.add(no.dado);
        inorder(no.dir, result);
    }

    // ── CSV loading ──────────────────────────────────────────────────────────

    static Map<Integer, Restaurante> carregarCSV() throws IOException {
        Map<Integer, Restaurante> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("/tmp/restaurantes.csv"));
        br.readLine(); // header
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.isBlank()) {
                Restaurante r = parseRestaurante(line);
                map.put(r.id, r);
            }
        }
        br.close();
        return map;
    }

    // ── Main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        Map<Integer, Restaurante> csv = carregarCSV();

        Scanner sc = new Scanner(System.in);

        // Read IDs, build BST
        while (sc.hasNextInt()) {
            int id = sc.nextInt();
            if (id == -1) break;
            Restaurante r = csv.get(id);
            if (r != null) inserir(r);
        }
        sc.nextLine(); // consume newline after -1

        // Search queries
        long inicio = System.currentTimeMillis();
        long comparacoes = 0, movimentacoes = 0;

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        while (sc.hasNextLine()) {
            String query = sc.nextLine().trim();
            if (query.equals("FIM")) break;
            pw.println(buscar(query));
        }

        // In-order
        List<Restaurante> sorted = new ArrayList<>();
        inorder(raiz, sorted);
        for (Restaurante r : sorted) {
            pw.println(r.formatar());
        }
        pw.flush();

        long fim = System.currentTimeMillis();
        double tempo = fim - inicio;

        // Log
        PrintWriter log = new PrintWriter(new FileWriter("797798_busca.txt"));
        log.printf("%s\t%d\t%d\t%.2f%n", MATRICULA, comparacoes, movimentacoes, tempo);
        log.close();
    }
}

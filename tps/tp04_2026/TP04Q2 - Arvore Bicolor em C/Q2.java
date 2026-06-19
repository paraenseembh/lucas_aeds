import java.io.*;
import java.util.*;

public class Q2 {

    static final String MATRICULA = "868511";

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

    // ── Red-Black Tree ───────────────────────────────────────────────────────

    static final boolean VERMELHO = true;
    static final boolean PRETO    = false;

    static class No {
        Restaurante dado;
        boolean cor;
        No esq, dir, pai;

        No(Restaurante r) {
            dado = r;
            cor  = VERMELHO;
        }
    }

    static No NIL;   // sentinela
    static No raiz;

    static void initNIL() {
        NIL = new No(null);
        NIL.cor = PRETO;
        NIL.esq = NIL.dir = NIL.pai = NIL;
        raiz = NIL;
    }

    static void rotEsq(No x) {
        No y = x.dir;
        x.dir = y.esq;
        if (y.esq != NIL) y.esq.pai = x;
        y.pai = x.pai;
        if (x.pai == NIL)         raiz = y;
        else if (x == x.pai.esq) x.pai.esq = y;
        else                      x.pai.dir = y;
        y.esq = x;
        x.pai = y;
    }

    static void rotDir(No x) {
        No y = x.esq;
        x.esq = y.dir;
        if (y.dir != NIL) y.dir.pai = x;
        y.pai = x.pai;
        if (x.pai == NIL)         raiz = y;
        else if (x == x.pai.dir) x.pai.dir = y;
        else                      x.pai.esq = y;
        y.dir = x;
        x.pai = y;
    }

    static void fixInsert(No z) {
        while (z.pai.cor == VERMELHO) {
            if (z.pai == z.pai.pai.esq) {
                No y = z.pai.pai.dir;
                if (y.cor == VERMELHO) {        // caso 1
                    z.pai.cor     = PRETO;
                    y.cor         = PRETO;
                    z.pai.pai.cor = VERMELHO;
                    z = z.pai.pai;
                } else {
                    if (z == z.pai.dir) {        // caso 2 → 3
                        z = z.pai;
                        rotEsq(z);
                    }
                    z.pai.cor     = PRETO;       // caso 3
                    z.pai.pai.cor = VERMELHO;
                    rotDir(z.pai.pai);
                }
            } else {
                No y = z.pai.pai.esq;
                if (y.cor == VERMELHO) {
                    z.pai.cor     = PRETO;
                    y.cor         = PRETO;
                    z.pai.pai.cor = VERMELHO;
                    z = z.pai.pai;
                } else {
                    if (z == z.pai.esq) {
                        z = z.pai;
                        rotDir(z);
                    }
                    z.pai.cor     = PRETO;
                    z.pai.pai.cor = VERMELHO;
                    rotEsq(z.pai.pai);
                }
            }
        }
        raiz.cor = PRETO;
    }

    static void inserir(Restaurante r) {
        No z = new No(r);
        z.esq = z.dir = z.pai = NIL;

        No y = NIL, x = raiz;
        while (x != NIL) {
            y = x;
            int cmp = r.nome.compareTo(x.dado.nome);
            if      (cmp < 0) x = x.esq;
            else if (cmp > 0) x = x.dir;
            else return; // duplicado
        }
        z.pai = y;
        if (y == NIL)                              raiz = z;
        else if (r.nome.compareTo(y.dado.nome) < 0) y.esq = z;
        else                                         y.dir = z;
        fixInsert(z);
    }

    static String buscar(String nome) {
        StringBuilder path = new StringBuilder("raiz");
        No x = raiz;
        while (x != NIL) {
            int cmp = nome.compareTo(x.dado.nome);
            if (cmp == 0) { path.append(" SIM"); return path.toString(); }
            if (cmp < 0)  { path.append(" esq"); x = x.esq; }
            else           { path.append(" dir"); x = x.dir; }
        }
        path.append(" NAO");
        return path.toString();
    }

    // ── In-order traversal ───────────────────────────────────────────────────

    static void inorder(No no, List<Restaurante> result) {
        if (no == NIL) return;
        inorder(no.esq, result);
        result.add(no.dado);
        inorder(no.dir, result);
    }

    // ── CSV loading ──────────────────────────────────────────────────────────

    static Map<Integer, Restaurante> carregarCSV() throws IOException {
        Map<Integer, Restaurante> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("/tmp/restaurantes.csv"));
        br.readLine();
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
        initNIL();

        Map<Integer, Restaurante> csv = carregarCSV();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextInt()) {
            int id = sc.nextInt();
            if (id == -1) break;
            Restaurante r = csv.get(id);
            if (r != null) inserir(r);
        }
        sc.nextLine();

        long inicio = System.currentTimeMillis();
        long comparacoes = 0;

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        while (sc.hasNextLine()) {
            String query = sc.nextLine().trim();
            if (query.equals("FIM")) break;
            pw.println(buscar(query));
        }

        List<Restaurante> sorted = new ArrayList<>();
        inorder(raiz, sorted);
        for (Restaurante r : sorted) {
            pw.println(r.formatar());
        }
        pw.flush();

        long fim = System.currentTimeMillis();
        double tempo = fim - inicio;

        PrintWriter log = new PrintWriter(new FileWriter(MATRICULA + "_arvore_bicolor.txt"));
        log.printf("%s\t%d\t%.2f%n", MATRICULA, comparacoes, tempo);
        log.close();
    }
}

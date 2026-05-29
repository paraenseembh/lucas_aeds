import java.io.*;
import java.util.*;

public class Q1 {

    static final String MATRICULA = "797798";

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

    // ── AVL ──────────────────────────────────────────────────────────────────

    static class No {
        Restaurante dado;
        No esq, dir;
        int altura;
        No(Restaurante r) { dado = r; altura = 1; }
    }

    static No raiz = null;

    static int altura(No no) { return no == null ? 0 : no.altura; }

    static void atualizarAltura(No no) {
        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
    }

    static int fatorBalanco(No no) {
        return no == null ? 0 : altura(no.esq) - altura(no.dir);
    }

    static No rotacionarDireita(No y) {
        No x = y.esq;
        No T2 = x.dir;
        x.dir = y;
        y.esq = T2;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    static No rotacionarEsquerda(No x) {
        No y = x.dir;
        No T2 = y.esq;
        y.esq = x;
        x.dir = T2;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    static No inserir(No no, Restaurante r) {
        if (no == null) return new No(r);
        int cmp = r.nome.compareTo(no.dado.nome);
        if (cmp < 0)      no.esq = inserir(no.esq, r);
        else if (cmp > 0) no.dir = inserir(no.dir, r);
        else              return no;

        atualizarAltura(no);
        int bal = fatorBalanco(no);

        // LL
        if (bal > 1 && r.nome.compareTo(no.esq.dado.nome) < 0)
            return rotacionarDireita(no);
        // RR
        if (bal < -1 && r.nome.compareTo(no.dir.dado.nome) > 0)
            return rotacionarEsquerda(no);
        // LR
        if (bal > 1 && r.nome.compareTo(no.esq.dado.nome) > 0) {
            no.esq = rotacionarEsquerda(no.esq);
            return rotacionarDireita(no);
        }
        // RL
        if (bal < -1 && r.nome.compareTo(no.dir.dado.nome) < 0) {
            no.dir = rotacionarDireita(no.dir);
            return rotacionarEsquerda(no);
        }

        return no;
    }

    static String buscar(String nome) {
        StringBuilder path = new StringBuilder("raiz");
        No atual = raiz;
        while (atual != null) {
            int cmp = nome.compareTo(atual.dado.nome);
            if (cmp == 0) { path.append(" SIM"); return path.toString(); }
            if (cmp < 0)  { path.append(" esq"); atual = atual.esq; }
            else          { path.append(" dir"); atual = atual.dir; }
        }
        path.append(" NAO");
        return path.toString();
    }

    static void inorder(No no, List<Restaurante> result) {
        if (no == null) return;
        inorder(no.esq, result);
        result.add(no.dado);
        inorder(no.dir, result);
    }

    // ── CSV ──────────────────────────────────────────────────────────────────

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
        Map<Integer, Restaurante> csv = carregarCSV();
        Scanner sc = new Scanner(System.in);

        long inicio = System.currentTimeMillis();

        while (sc.hasNextInt()) {
            int id = sc.nextInt();
            if (id == -1) break;
            Restaurante r = csv.get(id);
            if (r != null) raiz = inserir(raiz, r);
        }
        sc.nextLine();

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

        PrintWriter log = new PrintWriter(new FileWriter(MATRICULA + "_avl.txt"));
        log.printf("%s\t%d\t%d\t%.2fms%n", MATRICULA, 0L, 0L, tempo);
        log.close();
    }
}

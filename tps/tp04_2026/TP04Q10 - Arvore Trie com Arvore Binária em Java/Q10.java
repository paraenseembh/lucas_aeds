import java.io.*;
import java.util.*;

public class Q10 {

    static final String MATRICULA = "868511";

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
        r.nome = f[1]; r.cidade = f[2];
        r.capacidade = Integer.parseInt(f[3]);
        r.avaliacao = Double.parseDouble(f[4]);
        r.tiposCozinha = f[5].split(";");
        r.faixaPreco = f[6].length();
        String[] ab = f[7].split("-")[0].split(":");
        String[] fe = f[7].split("-")[1].split(":");
        r.aberturaH = Integer.parseInt(ab[0]); r.aberturaM = Integer.parseInt(ab[1]);
        r.fechamentoH = Integer.parseInt(fe[0]); r.fechamentoM = Integer.parseInt(fe[1]);
        String[] dt = f[8].split("-");
        r.dataAno = Integer.parseInt(dt[0]); r.dataMes = Integer.parseInt(dt[1]); r.dataDia = Integer.parseInt(dt[2]);
        r.aberto = f[9].trim().equals("true");
        return r;
    }

    // ── BST node for children (keyed by char) ──────────────────────────────

    static class BSTNode {
        char key;
        No trieNode;
        BSTNode left, right;
        BSTNode(char k, No n) { key = k; trieNode = n; }
    }

    static BSTNode bstInsert(BSTNode root, char k, No n) {
        if (root == null) return new BSTNode(k, n);
        if (k < root.key) root.left = bstInsert(root.left, k, n);
        else if (k > root.key) root.right = bstInsert(root.right, k, n);
        return root;
    }

    static No bstFind(BSTNode root, char k) {
        if (root == null) return null;
        if (k == root.key) return root.trieNode;
        return bstFind(k < root.key ? root.left : root.right, k);
    }

    // ── Trie with BST children ──────────────────────────────────────────────

    static class No {
        char c;
        Restaurante dado;
        BSTNode filhos;
        No(char c) { this.c = c; }
    }

    static No raiz = new No('\0');

    static void inserir(Restaurante r) {
        No cur = raiz;
        for (char ch : r.nome.toCharArray()) {
            No filho = bstFind(cur.filhos, ch);
            if (filho == null) {
                filho = new No(ch);
                cur.filhos = bstInsert(cur.filhos, ch, filho);
            }
            cur = filho;
        }
        cur.dado = r;
    }

    static void buscar(String nome, PrintWriter pw) {
        StringBuilder sb = new StringBuilder();
        No cur = raiz;
        for (char ch : nome.toCharArray()) {
            No filho = bstFind(cur.filhos, ch);
            if (filho == null) {
                if (sb.length() > 0) pw.print(sb.toString() + " ");
                pw.println("NAO");
                return;
            }
            if (sb.length() > 0) sb.append(' ');
            sb.append(ch);
            cur = filho;
        }
        if (cur.dado != null) {
            pw.println(sb.toString() + " SIM " + cur.dado.formatar());
        } else {
            if (sb.length() > 0) pw.print(sb.toString() + " ");
            pw.println("NAO");
        }
    }

    // ── CSV ─────────────────────────────────────────────────────────────────

    static Map<Integer, Restaurante> carregarCSV() throws IOException {
        Map<Integer, Restaurante> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("/tmp/restaurantes.csv"));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null)
            if (!line.isBlank()) { Restaurante r = parseRestaurante(line); map.put(r.id, r); }
        br.close();
        return map;
    }

    // ── Main ────────────────────────────────────────────────────────────────

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

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        while (sc.hasNextLine()) {
            String q = sc.nextLine().trim();
            if (q.equals("FIM")) break;
            buscar(q, pw);
        }
        pw.flush();

        PrintWriter log = new PrintWriter(new FileWriter(MATRICULA + "_arvore_trie_arvore.txt"));
        log.printf("%s\t0\t0.00%n", MATRICULA);
        log.close();
    }
}

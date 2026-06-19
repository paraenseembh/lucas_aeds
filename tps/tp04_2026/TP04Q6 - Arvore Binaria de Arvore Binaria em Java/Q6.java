import java.io.*;
import java.util.*;

public class Q6 {

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

    // ── L2: AVL keyed by nome ───────────────────────────────────────────────

    static class L2Node {
        Restaurante dado;
        L2Node left, right;
        int h;
        L2Node(Restaurante r) { dado = r; h = 0; }
    }

    static int ht(L2Node n) { return n == null ? -1 : n.h; }
    static void upd(L2Node n) { if (n != null) n.h = 1 + Math.max(ht(n.left), ht(n.right)); }
    static int bal(L2Node n) { return n == null ? 0 : ht(n.right) - ht(n.left); }

    static L2Node rotLeft(L2Node x) {
        L2Node y = x.right; x.right = y.left; y.left = x; upd(x); upd(y); return y;
    }
    static L2Node rotRight(L2Node x) {
        L2Node y = x.left; x.left = y.right; y.right = x; upd(x); upd(y); return y;
    }

    static L2Node avlInsert(L2Node nd, Restaurante r) {
        if (nd == null) return new L2Node(r);
        int cmp = r.nome.compareTo(nd.dado.nome);
        if (cmp < 0) nd.left = avlInsert(nd.left, r);
        else if (cmp > 0) nd.right = avlInsert(nd.right, r);
        else return nd;
        upd(nd);
        int b = bal(nd);
        if (b > 1) {
            if (r.nome.compareTo(nd.right.dado.nome) > 0) return rotLeft(nd);
            nd.right = rotRight(nd.right); return rotLeft(nd);
        }
        if (b < -1) {
            if (r.nome.compareTo(nd.left.dado.nome) < 0) return rotRight(nd);
            nd.left = rotLeft(nd.left); return rotRight(nd);
        }
        return nd;
    }

    // L2 search: print lowercase path (including direction to null), return found data or null
    static Restaurante avlSearch(L2Node root, String nome, StringBuilder sb) {
        sb.append("raiz");
        L2Node cur = root;
        while (cur != null) {
            int cmp = nome.compareTo(cur.dado.nome);
            if (cmp == 0) return cur.dado;
            else if (cmp < 0) { sb.append(" esq"); cur = cur.left; }
            else { sb.append(" dir"); cur = cur.right; }
        }
        return null;
    }

    // ── L1: plain BST keyed by cap%15 ──────────────────────────────────────

    static class L1Node {
        int key;
        L2Node l2;
        L1Node left, right;
        L1Node(int k) { key = k; }
    }

    static L1Node l1Root = null;

    static L1Node l1Insert(L1Node root, int key) {
        if (root == null) return new L1Node(key);
        if (key < root.key) root.left = l1Insert(root.left, key);
        else if (key > root.key) root.right = l1Insert(root.right, key);
        return root;
    }

    static L1Node l1Find(L1Node root, int key) {
        if (root == null) return null;
        if (key == root.key) return root;
        return l1Find(key < root.key ? root.left : root.right, key);
    }

    static void inserir(Restaurante r) {
        int key = r.capacidade % 15;
        L1Node n = l1Find(l1Root, key);
        if (n == null) { l1Root = l1Insert(l1Root, key); n = l1Find(l1Root, key); }
        n.l2 = avlInsert(n.l2, r);
    }

    // ── Preorder search ─────────────────────────────────────────────────────

    static String foundResult = null;

    static void preorder(L1Node node, String l1Label, String nome, StringBuilder sb) {
        if (foundResult != null) return;
        if (sb.length() > 0) sb.append(' ');
        sb.append(l1Label);
        if (node != null) {
            sb.append(' ');
            Restaurante found = avlSearch(node.l2, nome, sb);
            if (found != null) {
                sb.append(" SIM ").append(found.formatar());
                foundResult = sb.toString();
                return;
            }
            preorder(node.left, "ESQ", nome, sb);
            if (foundResult == null) preorder(node.right, "DIR", nome, sb);
        }
    }

    static String buscar(String nome) {
        foundResult = null;
        StringBuilder sb = new StringBuilder();
        preorder(l1Root, "RAIZ", nome, sb);
        if (foundResult != null) return foundResult;
        return sb.toString() + " NAO";
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
            pw.println(buscar(q));
        }
        pw.flush();

        PrintWriter log = new PrintWriter(new FileWriter(MATRICULA + "_hibrida_arvore_arvore.txt"));
        log.printf("%s\t0\t0.00%n", MATRICULA);
        log.close();
    }
}

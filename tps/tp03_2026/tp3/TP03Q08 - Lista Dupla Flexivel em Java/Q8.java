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

/* Lista Dupla Flexivel (doubly linked list, 0-indexed positions) */
class ListaDupla {
    static class No {
        Restaurante dado;
        No anterior, proximo;
        No(Restaurante dado) { this.dado = dado; }
    }

    No cabeca, cauda;
    int tamanho;

    public void inserirInicio(Restaurante r) {
        No novo = new No(r);
        novo.proximo = cabeca;
        if (cabeca != null) cabeca.anterior = novo;
        cabeca = novo;
        if (cauda == null) cauda = novo;
        tamanho++;
    }

    public void inserirFim(Restaurante r) {
        No novo = new No(r);
        novo.anterior = cauda;
        if (cauda != null) cauda.proximo = novo;
        cauda = novo;
        if (cabeca == null) cabeca = novo;
        tamanho++;
    }

    public void inserirPos(int pos, Restaurante r) {
        if (pos <= 0) { inserirInicio(r); return; }
        No atual = cabeca;
        for (int i = 0; i < pos - 1 && atual != null && atual.proximo != null; i++)
            atual = atual.proximo;
        if (atual == null) { inserirFim(r); return; }
        if (atual.proximo == null) { inserirFim(r); return; }
        No novo = new No(r);
        novo.proximo = atual.proximo;
        novo.anterior = atual;
        if (atual.proximo != null) atual.proximo.anterior = novo;
        else cauda = novo;
        atual.proximo = novo;
        tamanho++;
    }

    public Restaurante removerInicio() {
        if (cabeca == null) return null;
        Restaurante r = cabeca.dado;
        cabeca = cabeca.proximo;
        if (cabeca != null) cabeca.anterior = null;
        else cauda = null;
        tamanho--;
        return r;
    }

    public Restaurante removerFim() {
        if (cauda == null) return null;
        Restaurante r = cauda.dado;
        cauda = cauda.anterior;
        if (cauda != null) cauda.proximo = null;
        else cabeca = null;
        tamanho--;
        return r;
    }

    public Restaurante removerPos(int pos) {
        if (pos <= 0) return removerInicio();
        No atual = cabeca;
        for (int i = 0; i < pos && atual != null; i++) atual = atual.proximo;
        if (atual == null) return null;
        Restaurante r = atual.dado;
        if (atual.anterior != null) atual.anterior.proximo = atual.proximo;
        else cabeca = atual.proximo;
        if (atual.proximo != null) atual.proximo.anterior = atual.anterior;
        else cauda = atual.anterior;
        tamanho--;
        return r;
    }
}

public class Q8 {
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
        ListaDupla lista = new ListaDupla();
        int id;
        while ((id = sc.nextInt()) != -1)
            if (mapa.containsKey(id)) lista.inserirFim(mapa.get(id));

        long comparacoes = 0, movimentacoes = 0;
        long ini = System.currentTimeMillis();

        int n_ops = sc.nextInt();
        for (int i = 0; i < n_ops; i++) {
            String op = sc.next();
            if (op.equals("II")) {
                id = sc.nextInt();
                if (mapa.containsKey(id)) { lista.inserirInicio(mapa.get(id)); movimentacoes++; }
            } else if (op.equals("IF")) {
                id = sc.nextInt();
                if (mapa.containsKey(id)) { lista.inserirFim(mapa.get(id)); movimentacoes++; }
            } else if (op.equals("I*")) {
                int pos = sc.nextInt();
                id = sc.nextInt();
                if (mapa.containsKey(id)) { lista.inserirPos(pos, mapa.get(id)); movimentacoes++; }
            } else if (op.equals("RI")) {
                Restaurante r = lista.removerInicio();
                if (r != null) { System.out.println("(R)" + r.getNome()); movimentacoes++; }
            } else if (op.equals("RF")) {
                Restaurante r = lista.removerFim();
                if (r != null) { System.out.println("(R)" + r.getNome()); movimentacoes++; }
            } else if (op.equals("R*")) {
                int pos = sc.nextInt();
                Restaurante r = lista.removerPos(pos);
                if (r != null) { System.out.println("(R)" + r.getNome()); movimentacoes++; }
            }
        }

        long fim = System.currentTimeMillis();

        ListaDupla.No n = lista.cabeca;
        while (n != null) {
            System.out.println(n.dado.formatar());
            n = n.proximo;
        }

        PrintWriter log = new PrintWriter("797798_lista.txt");
        log.printf("797798\t%d\t%d\t%.2f%n", comparacoes, movimentacoes, (fim - ini) / 1000.0);
        log.close();
    }
}

import java.util.*;
import java.io.*;

class Data {
    private int ano, mes, dia;
    public Data(int ano, int mes, int dia) { this.ano=ano; this.mes=mes; this.dia=dia; }
    public int getAno() { return ano; }
    public int getMes() { return mes; }
    public int getDia() { return dia; }
    public static Data parseData(String s) {
        String[] p = s.split("-");
        return new Data(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
    }
    public String formatar() { return String.format("%02d/%02d/%04d", dia, mes, ano); }
}

class Hora {
    private int hora, minuto;
    public Hora(int hora, int minuto) { this.hora=hora; this.minuto=minuto; }
    public int getHora() { return hora; }
    public int getMinuto() { return minuto; }
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

    public Restaurante() {}
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCidade() { return cidade; }
    public int getCapacidade() { return capacidade; }
    public double getAvaliacao() { return avaliacao; }
    public String[] getTiposCozinha() { return tiposCozinha; }
    public int getFaixaPreco() { return faixaPreco; }
    public Hora getHorarioAbertura() { return horarioAbertura; }
    public Hora getHorarioFechamento() { return horarioFechamento; }
    public Data getDataAbertura() { return dataAbertura; }
    public boolean isAberto() { return aberto; }

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

class ColecaoRestaurantes {
    private int tamanho;
    private Restaurante[] restaurantes;
    public int getTamanho() { return tamanho; }
    public Restaurante[] getRestaurantes() { return restaurantes; }
    public void lerCsv(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            br.readLine();
            String line;
            List<Restaurante> lista = new ArrayList<>();
            while ((line = br.readLine()) != null)
                if (!line.trim().isEmpty()) lista.add(Restaurante.parseRestaurante(line));
            br.close();
            tamanho = lista.size();
            restaurantes = lista.toArray(new Restaurante[0]);
        } catch (Exception e) { e.printStackTrace(); }
    }
    public static ColecaoRestaurantes lerCsv() {
        ColecaoRestaurantes c = new ColecaoRestaurantes();
        c.lerCsv("/tmp/restaurantes.csv");
        return c;
    }
}

public class Q1 {
    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();
        Restaurante[] rests = colecao.getRestaurantes();
        Scanner sc = new Scanner(System.in);
        int id;
        while ((id = sc.nextInt()) != -1) {
            for (Restaurante r : rests)
                if (r.getId() == id) { System.out.println(r.formatar()); break; }
        }
    }
}

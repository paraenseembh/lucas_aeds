import java.util.*;
import java.io.*;

class Data {
    private int ano, mes, dia;
    public Data(int ano, int mes, int dia){this.ano=ano;this.mes=mes;this.dia=dia;}
    public static Data parseData(String s){String[] p=s.split("-");return new Data(Integer.parseInt(p[0]),Integer.parseInt(p[1]),Integer.parseInt(p[2]));}
    public String formatar(){return String.format("%02d/%02d/%04d",dia,mes,ano);}
}
class Hora {
    private int hora, minuto;
    public Hora(int hora, int minuto){this.hora=hora;this.minuto=minuto;}
    public static Hora parseHora(String s){String[] p=s.split(":");return new Hora(Integer.parseInt(p[0]),Integer.parseInt(p[1]));}
    public String formatar(){return String.format("%02d:%02d",hora,minuto);}
}
class Restaurante {
    private int id; private String nome, cidade; private int capacidade; private double avaliacao;
    private String[] tiposCozinha; private int faixaPreco;
    private Hora horarioAbertura, horarioFechamento; private Data dataAbertura; private boolean aberto;
    public Restaurante(){}
    public int getId(){return id;} public String getNome(){return nome;}
    public static Restaurante parseRestaurante(String s){
        String[] p=s.split(","); Restaurante r=new Restaurante();
        r.id=Integer.parseInt(p[0]);r.nome=p[1];r.cidade=p[2];
        r.capacidade=Integer.parseInt(p[3]);r.avaliacao=Double.parseDouble(p[4]);
        r.tiposCozinha=p[5].split(";");r.faixaPreco=p[6].length();
        String[] h=p[7].split("-");r.horarioAbertura=Hora.parseHora(h[0]);r.horarioFechamento=Hora.parseHora(h[1]);
        r.dataAbertura=Data.parseData(p[8]);r.aberto=Boolean.parseBoolean(p[9].trim());
        return r;
    }
}
class ColecaoRestaurantes {
    private int tamanho; private Restaurante[] restaurantes;
    public Restaurante[] getRestaurantes(){return restaurantes;}
    public void lerCsv(String path){
        try{
            BufferedReader br=new BufferedReader(new FileReader(path));br.readLine();
            String line; List<Restaurante> lista=new ArrayList<>();
            while((line=br.readLine())!=null) if(!line.trim().isEmpty()) lista.add(Restaurante.parseRestaurante(line));
            br.close();tamanho=lista.size();restaurantes=lista.toArray(new Restaurante[0]);
        }catch(Exception e){e.printStackTrace();}
    }
    public static ColecaoRestaurantes lerCsv(){
        ColecaoRestaurantes c=new ColecaoRestaurantes();c.lerCsv("/tmp/restaurantes.csv");return c;
    }
}

public class Q5 {
    static long comparacoes=0;

    static boolean buscaSequencial(Restaurante[] arr, int n, String nome){
        for(int i=0;i<n;i++){
            comparacoes++;
            if(arr[i].getNome().equals(nome)) return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes colecao=ColecaoRestaurantes.lerCsv();
        Restaurante[] todos=colecao.getRestaurantes();
        Scanner sc=new Scanner(System.in);
        List<Restaurante> lista=new ArrayList<>();
        int id;
        while((id=sc.nextInt())!=-1){
            for(Restaurante r:todos) if(r.getId()==id){lista.add(r);break;}
        }
        Restaurante[] arr=lista.toArray(new Restaurante[0]);
        sc.nextLine(); // consume rest of last int line

        long ini=System.nanoTime();
        StringBuilder out=new StringBuilder();
        String linha;
        while(sc.hasNextLine()){
            linha=sc.nextLine().trim();
            if(linha.equals("FIM")) break;
            if(linha.isEmpty()) continue;
            out.append(buscaSequencial(arr,arr.length,linha)?"SIM":"NAO").append("\n");
        }
        long fim=System.nanoTime();
        double tempo=(fim-ini)/1e6;
        System.out.print(out);
        PrintWriter pw=new PrintWriter("matricula_sequencial.txt");
        pw.printf("797798\t%d\t%.2f%n",comparacoes,tempo);
        pw.close();
    }
}

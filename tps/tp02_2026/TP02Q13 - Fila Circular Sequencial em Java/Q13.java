import java.util.*;
import java.io.*;

class Data {
    private int ano,mes,dia;
    public Data(int a,int m,int d){ano=a;mes=m;dia=d;}
    public int getAno(){return ano;}
    public static Data parseData(String s){String[] p=s.split("-");return new Data(Integer.parseInt(p[0]),Integer.parseInt(p[1]),Integer.parseInt(p[2]));}
    public String formatar(){return String.format("%02d/%02d/%04d",dia,mes,ano);}
}
class Hora {
    private int hora,minuto;
    public Hora(int h,int m){hora=h;minuto=m;}
    public static Hora parseHora(String s){String[] p=s.split(":");return new Hora(Integer.parseInt(p[0]),Integer.parseInt(p[1]));}
    public String formatar(){return String.format("%02d:%02d",hora,minuto);}
}
class Restaurante {
    private int id;private String nome,cidade;private int capacidade;private double avaliacao;
    private String[] tiposCozinha;private int faixaPreco;
    private Hora horarioAbertura,horarioFechamento;private Data dataAbertura;private boolean aberto;
    public Restaurante(){}
    public int getId(){return id;}public String getNome(){return nome;}
    public Data getDataAbertura(){return dataAbertura;}
    public static Restaurante parseRestaurante(String s){
        String[] p=s.split(",");Restaurante r=new Restaurante();
        r.id=Integer.parseInt(p[0]);r.nome=p[1];r.cidade=p[2];
        r.capacidade=Integer.parseInt(p[3]);r.avaliacao=Double.parseDouble(p[4]);
        r.tiposCozinha=p[5].split(";");r.faixaPreco=p[6].length();
        String[] h=p[7].split("-");r.horarioAbertura=Hora.parseHora(h[0]);r.horarioFechamento=Hora.parseHora(h[1]);
        r.dataAbertura=Data.parseData(p[8]);r.aberto=Boolean.parseBoolean(p[9].trim());return r;
    }
    public String formatar(){
        StringBuilder sb=new StringBuilder();
        sb.append("[").append(id).append(" ## ").append(nome).append(" ## ").append(cidade).append(" ## ");
        sb.append(capacidade).append(" ## ").append(String.format(java.util.Locale.US,"%.1f",avaliacao)).append(" ## [");
        for(int i=0;i<tiposCozinha.length;i++){if(i>0)sb.append(",");sb.append(tiposCozinha[i]);}
        sb.append("] ## ");for(int i=0;i<faixaPreco;i++)sb.append("$");
        sb.append(" ## ").append(horarioAbertura.formatar()).append("-").append(horarioFechamento.formatar());
        sb.append(" ## ").append(dataAbertura.formatar()).append(" ## ").append(aberto).append("]");
        return sb.toString();
    }
}
class ColecaoRestaurantes {
    private Restaurante[] restaurantes;
    public Restaurante[] getRestaurantes(){return restaurantes;}
    public void lerCsv(String path){
        try{
            BufferedReader br=new BufferedReader(new FileReader(path));br.readLine();
            String line;List<Restaurante> lista=new ArrayList<>();
            while((line=br.readLine())!=null)if(!line.trim().isEmpty())lista.add(Restaurante.parseRestaurante(line));
            br.close();restaurantes=lista.toArray(new Restaurante[0]);
        }catch(Exception e){e.printStackTrace();}
    }
    public static ColecaoRestaurantes lerCsv(){
        ColecaoRestaurantes c=new ColecaoRestaurantes();c.lerCsv("/tmp/restaurantes.csv");return c;
    }
}

class FilaCircular {
    private static final int MAX=5;
    private Restaurante[] arr=new Restaurante[MAX];
    private int inicio=0, fim=0, tamanho=0;

    public boolean cheia(){ return tamanho==MAX; }
    public boolean vazia(){ return tamanho==0; }

    public Restaurante desenfileirar(){
        Restaurante r=arr[inicio];
        inicio=(inicio+1)%MAX;
        tamanho--;
        return r;
    }

    public void enfileirar(Restaurante r){
        arr[fim]=r;
        fim=(fim+1)%MAX;
        tamanho++;
    }

    public long mediaAno(){
        long soma=0;
        for(int i=0;i<tamanho;i++) soma+=arr[(inicio+i)%MAX].getDataAbertura().getAno();
        return Math.round((double)soma/tamanho);
    }

    public int tamanho(){ return tamanho; }
    public Restaurante get(int i){ return arr[(inicio+i)%MAX]; }
}

public class Q13 {
    static Restaurante[] todos;

    static Restaurante buscar(int id){
        for(Restaurante r:todos) if(r.getId()==id) return r;
        return null;
    }

    static void inserir(FilaCircular fila, Restaurante r){
        if(fila.cheia()){
            Restaurante rem=fila.desenfileirar();
            System.out.println("(R)"+rem.getNome());
        }
        fila.enfileirar(r);
        System.out.println("(I)"+fila.mediaAno());
    }

    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes colecao=ColecaoRestaurantes.lerCsv();
        todos=colecao.getRestaurantes();
        Scanner sc=new Scanner(System.in);
        FilaCircular fila=new FilaCircular();
        int id;
        while((id=sc.nextInt())!=-1){
            Restaurante r=buscar(id);
            if(r!=null) inserir(fila,r);
        }
        int n=sc.nextInt();
        for(int op=0;op<n;op++){
            String cmd=sc.next();
            if(cmd.equals("I")){
                int rid=sc.nextInt();
                Restaurante r=buscar(rid);
                if(r!=null) inserir(fila,r);
            } else {
                Restaurante r=fila.desenfileirar();
                System.out.println("(R)"+r.getNome());
            }
        }
        for(int i=0;i<fila.tamanho();i++) System.out.println(fila.get(i).formatar());
    }
}

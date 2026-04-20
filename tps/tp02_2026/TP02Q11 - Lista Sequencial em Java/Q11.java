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

class Lista {
    private Restaurante[] arr;
    private int n;
    public Lista(int max){arr=new Restaurante[max];n=0;}

    public void inserirInicio(Restaurante r){
        for(int i=n;i>0;i--) arr[i]=arr[i-1];
        arr[0]=r; n++;
    }
    public void inserir(Restaurante r, int pos){
        for(int i=n;i>pos;i--) arr[i]=arr[i-1];
        arr[pos]=r; n++;
    }
    public void inserirFim(Restaurante r){ arr[n++]=r; }

    public Restaurante removerInicio(){
        Restaurante r=arr[0];
        for(int i=0;i<n-1;i++) arr[i]=arr[i+1];
        n--; return r;
    }
    public Restaurante remover(int pos){
        Restaurante r=arr[pos];
        for(int i=pos;i<n-1;i++) arr[i]=arr[i+1];
        n--; return r;
    }
    public Restaurante removerFim(){ return arr[--n]; }
    public int tamanho(){ return n; }
    public Restaurante get(int i){ return arr[i]; }
}

public class Q11 {
    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes colecao=ColecaoRestaurantes.lerCsv();
        Restaurante[] todos=colecao.getRestaurantes();
        Scanner sc=new Scanner(System.in);
        Lista lista=new Lista(200);
        int id;
        while((id=sc.nextInt())!=-1){
            for(Restaurante r:todos)if(r.getId()==id){lista.inserirFim(r);break;}
        }
        int n=sc.nextInt();
        for(int op=0;op<n;op++){
            String cmd=sc.next();
            if(cmd.equals("II")){
                int rid=sc.nextInt();
                for(Restaurante r:todos)if(r.getId()==rid){lista.inserirInicio(r);break;}
            } else if(cmd.equals("IF")){
                int rid=sc.nextInt();
                for(Restaurante r:todos)if(r.getId()==rid){lista.inserirFim(r);break;}
            } else if(cmd.equals("I*")){
                int pos=sc.nextInt(); int rid=sc.nextInt();
                for(Restaurante r:todos)if(r.getId()==rid){lista.inserir(r,pos);break;}
            } else if(cmd.equals("RI")){
                Restaurante r=lista.removerInicio();
                System.out.println("(R)"+r.getNome());
            } else if(cmd.equals("RF")){
                Restaurante r=lista.removerFim();
                System.out.println("(R)"+r.getNome());
            } else if(cmd.equals("R*")){
                int pos=sc.nextInt();
                Restaurante r=lista.remover(pos);
                System.out.println("(R)"+r.getNome());
            }
        }
        for(int i=0;i<lista.tamanho();i++) System.out.println(lista.get(i).formatar());
    }
}

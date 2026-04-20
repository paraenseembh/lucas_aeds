import java.util.*;
import java.io.*;

class Data {
    private int ano,mes,dia;
    public Data(int a,int m,int d){ano=a;mes=m;dia=d;}
    public int getAno(){return ano;}public int getMes(){return mes;}public int getDia(){return dia;}
    public static Data parseData(String s){String[] p=s.split("-");return new Data(Integer.parseInt(p[0]),Integer.parseInt(p[1]),Integer.parseInt(p[2]));}
    public String formatar(){return String.format("%02d/%02d/%04d",dia,mes,ano);}
    public int compareTo(Data o){
        if(ano!=o.ano) return ano-o.ano;
        if(mes!=o.mes) return mes-o.mes;
        return dia-o.dia;
    }
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
    public int getId(){return id;}public String getNome(){return nome;}public String getCidade(){return cidade;}
    public int getCapacidade(){return capacidade;}public double getAvaliacao(){return avaliacao;}
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

public class Q9 {
    static long comparacoes=0, movimentacoes=0;

    static int compara(Restaurante a, Restaurante b){
        int c=a.getDataAbertura().compareTo(b.getDataAbertura());
        if(c!=0) return c;
        return a.getNome().compareTo(b.getNome());
    }

    static void heapify(Restaurante[] arr, int n, int i){
        int maior=i, esq=2*i+1, dir=2*i+2;
        if(esq<n){comparacoes++;if(compara(arr[esq],arr[maior])>0)maior=esq;}
        if(dir<n){comparacoes++;if(compara(arr[dir],arr[maior])>0)maior=dir;}
        if(maior!=i){
            Restaurante tmp=arr[i];arr[i]=arr[maior];arr[maior]=tmp;movimentacoes+=3;
            heapify(arr,n,maior);
        }
    }

    static void heapsort(Restaurante[] arr, int n){
        for(int i=n/2-1;i>=0;i--) heapify(arr,n,i);
        for(int i=n-1;i>0;i--){
            Restaurante tmp=arr[0];arr[0]=arr[i];arr[i]=tmp;movimentacoes+=3;
            heapify(arr,i,0);
        }
    }

    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes colecao=ColecaoRestaurantes.lerCsv();
        Restaurante[] todos=colecao.getRestaurantes();
        Scanner sc=new Scanner(System.in);
        List<Restaurante> lista=new ArrayList<>();
        int id;
        while((id=sc.nextInt())!=-1)
            for(Restaurante r:todos)if(r.getId()==id){lista.add(r);break;}
        Restaurante[] arr=lista.toArray(new Restaurante[0]);
        long ini=System.nanoTime();
        heapsort(arr,arr.length);
        long fim=System.nanoTime();
        double tempo=(fim-ini)/1e6;
        for(Restaurante r:arr) System.out.println(r.formatar());
        PrintWriter pw=new PrintWriter("matricula_heapsort.txt");
        pw.printf("797798\t%d\t%d\t%.2f%n",comparacoes,movimentacoes,tempo);
        pw.close();
    }
}

import java.util.*;
import java.io.*;

class Data {
    private int ano, mes, dia;
    public Data(int a,int m,int d){ano=a;mes=m;dia=d;}
    public int getAno(){return ano;}public int getMes(){return mes;}public int getDia(){return dia;}
    public static Data parseData(String s){String[] p=s.split("-");return new Data(Integer.parseInt(p[0]),Integer.parseInt(p[1]),Integer.parseInt(p[2]));}
    public String formatar(){return String.format("%02d/%02d/%04d",dia,mes,ano);}
}
class Hora {
    private int hora, minuto;
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

public class Q7 {
    static long comparacoes=0, movimentacoes=0;

    static int compara(Restaurante a, Restaurante b){
        int c=a.getCidade().compareTo(b.getCidade());
        if(c!=0) return c;
        return a.getNome().compareTo(b.getNome());
    }

    static void merge(Restaurante[] arr, Restaurante[] tmp, int lo, int mid, int hi){
        for(int k=lo;k<=hi;k++){tmp[k]=arr[k];movimentacoes++;}
        int i=lo,j=mid+1;
        for(int k=lo;k<=hi;k++){
            comparacoes++;
            if(i>mid){arr[k]=tmp[j++];movimentacoes++;}
            else if(j>hi){arr[k]=tmp[i++];movimentacoes++;}
            else if(compara(tmp[i],tmp[j])<=0){arr[k]=tmp[i++];movimentacoes++;}
            else{arr[k]=tmp[j++];movimentacoes++;}
        }
    }

    static void mergesort(Restaurante[] arr, Restaurante[] tmp, int lo, int hi){
        if(lo>=hi) return;
        int mid=(lo+hi)/2;
        mergesort(arr,tmp,lo,mid);
        mergesort(arr,tmp,mid+1,hi);
        merge(arr,tmp,lo,mid,hi);
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
        Restaurante[] tmp=new Restaurante[arr.length];
        long ini=System.nanoTime();
        mergesort(arr,tmp,0,arr.length-1);
        long fim=System.nanoTime();
        double tempo=(fim-ini)/1e6;
        for(Restaurante r:arr) System.out.println(r.formatar());
        PrintWriter pw=new PrintWriter("matricula_mergesort.txt");
        pw.printf("797798\t%d\t%d\t%.2f%n",comparacoes,movimentacoes,tempo);
        pw.close();
    }
}

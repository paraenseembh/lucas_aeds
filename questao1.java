
import java.util.Scanner;


/*Análise de Dados de Corrida de Carros



Você foi contratado para realizar uma análise de 
dados acerca de um campeonato de corrida de carros. 
Para isso, você recebeu a listagem dos pilotos com nome
 (sem espaço), peso em Kg (inteiro) e o ranking (inteiro) 
 de cada piloto ao final do campeonato. Deseja-se analisar 
 se o peso do piloto tem relação com o seu ranking. 
 Para isso, você deve organizar e imprimir a relação dos pilotos,
  um por linha, ordenados dos mais leves aos mais pesados. 
Caso os pilotos tenham o mesmo peso,
organize pelo ranking mais bem colocado.



Requisitos:

- Criar uma estrutura de dados para representar um piloto.
- A primeira linha do arquivo de entrada contém a quantidade de pilotos. Cada linha subsequetente apresenta os dados de um piloto: Nome Peso Ranking
- O arquivo de saída deve estar no formato: Nome Peso Ranking */


class Piloto{
    String nome;
    int peso;
    int ranking;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public static void setPiloto(){
        new Piloto();
    }
    
}


public class questao1{
    private static Piloto[] bubble_sort(int tam, Piloto[] y){
        Piloto[] array = new Piloto();
        boolean status = false;
        int i = 0;
        while (i<9 && status == false){
            if (i >= 10){
                status = true;
            }
            if (i < tam && array[i].peso > array[i+1].peso){
                temp = array[i];
                array[i+1] = array[i];
                array[i] = temp;
            }
            i++;

        }
        return array; 
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tam = scanner.nextInt();
        String linha = "";
        Piloto[] array = new Piloto[tam];
        for(int i = 0; i < tam; i++){
            array[i] = new Piloto();
            array[i].nome = scanner.next();
            array[i].peso = scanner.nextInt();
            array[i].ranking = scanner.nextInt();
        }

        array = bubble_sort(tam, array);

        for (int i = 0; i < 10; i++) {
            System.out.println(array[i].getNome() + " " +  array[i].getPeso() + " " + array[i].getRanking());
        }
        /*System.out.println(array[7].getNome() + " " + array[7].getPeso() + " " + array[7].getRanking());
        System.out.println(array[5].getNome() + " " + array[5].getPeso() + " " + array[5].getRanking());
        System.out.println(array[3].getNome() + " " + array[3].getPeso() + " " + array[3].getRanking());
        System.out.println(array[1].getNome() + " " + array[1].getPeso() + " " + array[1].getRanking());
        System.out.println(array[0].getNome() + " " + array[0].getPeso() + " " + array[0].getRanking());
        System.out.println(array[8].getNome() + " " + array[8].getPeso() + " " + array[8].getRanking());
        System.out.println(array[2].getNome() + " " + array[2].getPeso() + " " + array[2].getRanking());
        System.out.println(array[9].getNome() + " " + array[9].getPeso() + " " + array[9].getRanking());
        System.out.println(array[4].getNome() + " " + array[4].getPeso() + " " + array[4].getRanking());
        System.out.println(array[6].getNome() + " " + array[6].getPeso() + " " + array[6].getRanking());
        scanner.close();*/


}



}
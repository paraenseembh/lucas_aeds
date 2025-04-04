import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Principal {
    
    
    public static void main (String[]args) throws FileNotFoundException{
        Show[] shows = new Show[1368];
    
        //Ler arquivo csv
        //e adicionar o show
        //de cada linha no array

        File file = new File("disneyplus.csv");
        Scanner scanner = new Scanner(file); 
        for(int i = 0 ; i < 1368; i++){
        System.out.println(scanner.nextLine());
        String linha = scanner.nextLine();
        }
        /*da pra fazer usando
         * while(scanner.hasnextLine){
         * System.out.println(scanner.nextLine());
         * }
         */
        //usar normalmente

        scanner.close();
    }
}

class Show{
    //atributos
    private String SHOW_ID; 
    private String TYPE;
    private String TITLE; 
    private String DIRECTOR;
    private String CAST; //REMONTAR PARA ARRAY
    private String COUNTRY;
    private String DATE_ADDED; 
    private String RELEASE_YEAR;
    private String RATING; // AQUI EH INT
    private String DURATION; 
    private String LISTED_IN;
    
    public String getSHOW_ID() {
        return SHOW_ID;
    }

    public void setSHOW_ID(String SHOW_ID) {
        this.SHOW_ID = SHOW_ID;
    }



    public static Show ler(String linha){
        Show novo_show = new Show();

        return novo_show;
    }

    public static void imprimir(){
        Show objeto = ler();
        println(ler(objeto.SHOW));
    }
   


}





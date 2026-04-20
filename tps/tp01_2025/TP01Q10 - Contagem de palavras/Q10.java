import java.util.*;

public class Q10 {
    public static boolean stop(String input) {
        boolean resp = false;

        if (input.length() == 3 && input.charAt(0) == 'F' && input.charAt(1) == 'I' && input.charAt(2) == 'M') 
        {
            resp = true;
        }

        return resp;
    }

    public static int contaPalavras(String input){
        int contador = 1;
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == ' '){
                contador++;
            }
        }
        return contador;
    }

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        input = scanner.nextLine();
        while(!stop(input)){
            if (!input.isBlank()){
            System.out.println(contaPalavras(input));}
            
            else {
                System.out.println("0");
            }

            input = scanner.nextLine();
        
        }

        scanner.close();
    }
        
}


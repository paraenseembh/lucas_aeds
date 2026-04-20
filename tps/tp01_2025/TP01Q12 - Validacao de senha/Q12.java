import java.util.*;

public class Q12 {
    public static boolean ehValida(String input){
        boolean maiuscula  = false;
        boolean minuscula  = false;
        boolean numero     = false;
        boolean especial   = false;

        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            if(Character.isUpperCase(c)){
                maiuscula = true;
            }
            if(Character.isLowerCase(c)){
                minuscula = true;
            }
            if(Character.isDigit(c)){
                numero = true;
            }
            if(Character.isLetterOrDigit(c)){
                especial = true;
            }
        }
        
        return (maiuscula && minuscula && numero && especial);
    }

    public static boolean stop(String input) {
        boolean resp = false;

        if (input.length() == 3 && input.charAt(0) == 'F' && input.charAt(1) == 'I' && input.charAt(2) == 'M') 
        {
            resp = true;
        }

        return resp;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String senha = "";
        do {
            senha = scanner.nextLine();
            if (ehValida(senha)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");}
            }while (!stop(senha));
            
        
        scanner.close();
    }
}


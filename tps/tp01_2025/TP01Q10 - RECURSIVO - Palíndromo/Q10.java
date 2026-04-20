import java.util.*;

public class Q10 {
    // Método não recursivo que verifica se a string de entrada é "FIM"
    public static boolean stop(String input) {
        return input.length() == 3 && input.charAt(0) == 'F' && 
               input.charAt(1) == 'I' && input.charAt(2) == 'M';
    }
    
    // Método wrapper que inicializa a recursão para verificar se é palíndromo
    public static boolean isPal(String input) {
        return isPal(input, 0);
    }
    
    // Método recursivo para verificar se é palíndromo
    public static boolean isPal(String input, int i) {
        boolean resp;
        
        if (i >= input.length() / 2) {
            resp = true;
        } else if (input.charAt(i) != input.charAt(input.length() - 1 - i)) {
            resp = false;
        } else {
            resp = isPal(input, i + 1);
        }
        
        return resp;
    }
    
    // Método wrapper que inicia a leitura e processamento recursivo das entradas
    public static void main(String[] args) {
        Scanner scanf = new Scanner(System.in);
        String input = scanf.nextLine();
        
        processInputs(scanf, input);
        
        scanf.close();
    }
    
    // Método recursivo para processar as entradas
    public static void processInputs(Scanner scanf, String input) {
        if (!stop(input)) {
            if (isPal(input)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
            
            processInputs(scanf, scanf.nextLine());
        }
    }
}
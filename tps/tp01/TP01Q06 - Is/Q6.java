import java.util.*;

public class Q6 {
    
    public static String retornaMinusculo(String input){
        String minusculo = "";
        for (int i = 0; i < input.length(); i++){
            if (input.charAt(i) >= 'A' && input.charAt(i) <= 'Z'){
                minusculo += (char) (input.charAt(i) + 32);
            } else {
                minusculo += input.charAt(i);
            }
        }
        return minusculo;
    }

    public static String retornaMaiusculo(String input) {
        String maiusculo = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) >= 'a' && input.charAt(i) <= 'z') {
                maiusculo += (char) (input.charAt(i) - 32);
            } else {
                maiusculo += input.charAt(i);
            }
        }
        return maiusculo;
    }

    public static String somenteVogais(String input){
        input = retornaMinusculo(input);
        for (int i = 0; i < input.length(); i++){
            if (input.charAt(i) != 'a' && input.charAt(i) != 'e' && input.charAt(i) != 'i' && 
                input.charAt(i) != 'o' && input.charAt(i) != 'u'){
                return "NAO";
            }
        }
        return "SIM";
    }
    
    public static String somenteConsoantes(String input){
        input = retornaMinusculo(input);
        if (input.isEmpty()) {
            return "NAO";
        }
        
        for (int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            // Verifica se não é uma letra
            if (c < 'a' || c > 'z') {
                return "NAO";
            }
            // Verifica se é uma vogal
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                return "NAO";
            }
        }
        return "SIM";
    }

    public static String isNumeroReal(String input) {
        if (input == null || input.isEmpty()) {
            return "NAO";
        }
        
        boolean temPonto = false;
        boolean temDigito = false;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            // Verificar se é dígito
            if (Character.isDigit(c)) {
                temDigito = true;
            }
            // Verificar ponto decimal
            else if (c == '.') {
                // Se já tem um ponto, não é número real válido
                if (temPonto) {
                    return "NAO";
                }
                temPonto = true;
            }
            // Se não é dígito nem ponto, não é número real
            else {
                return "NAO";
            }
        }
        
        // É número real se tem dígito e ponto decimal
        return (temDigito && temPonto) ? "SIM" : "NAO";
    }
    
    public static String eh_Int(String input){
        if (input == null || input.isEmpty()) {
            return "NAO";
        }
        
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return "NAO";
            }
        }
        return "SIM";
    }
    
    public static boolean stop(String input) {
        return input.length() == 3 && input.charAt(0) == 'F' && 
               input.charAt(1) == 'I' && input.charAt(2) == 'M';
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String linha = "";

        do {
            linha = scanner.nextLine();
            if (!stop(linha)) {
                System.out.println(retornaMaiusculo(somenteVogais(linha) + " " + somenteConsoantes(linha) + " " + eh_Int(linha) + " " + isNumeroReal(linha)));
            }
        } while (!stop(linha));

        scanner.close();
    }
}
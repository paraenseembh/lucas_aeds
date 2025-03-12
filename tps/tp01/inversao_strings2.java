import java.util.Scanner;


public class inversao_strings2 {
    
    public static boolean stop(String input) {
        boolean resp = false;

        if (input.length() == 3 && input.charAt(0) == 'F' && input.charAt(1) == 'I' && input.charAt(2) == 'M') 
        {
            resp = true;
        }

        return resp;}

    public static String inverteString(String input){
        int n = input.length();
        char ArrayPalavra[] = new char[1000];
        String resultado = "";
        for(int i = 0; i < n; i++){
            ArrayPalavra[i] = input.charAt(n-1-i);
            resultado += ArrayPalavra[i];
        }
        return resultado;
    
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while(!stop(input)){
            System.out.println(inverteString(input));
            input = scanner.nextLine();
        }
        scanner.close();
    }
}

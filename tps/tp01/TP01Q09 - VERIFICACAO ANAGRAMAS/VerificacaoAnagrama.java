import java.util.Scanner;

public class VerificacaoAnagrama {
    
    public static boolean saoAnagramas(String str1, String str2) {
        // Se as strings tiverem tamanhos diferentes, não podem ser anagramas
        if (str1.length() != str2.length()) {
            return false;
        }
        
        // Usando um array maior para acomodar caracteres Unicode
        // Ou podemos focar apenas nas letras do alfabeto
        int[] contador = new int[65536]; // Cobre todo o Basic Multilingual Plane do Unicode
        
        // Incrementando contadores para primeira string
        for (int i = 0; i < str1.length(); i++) {
            char c = str1.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char)(c + 32); // Converte para minúscula
            }
            contador[c]++;
        }
        
        // Decrementando contadores para segunda string
        for (int i = 0; i < str2.length(); i++) {
            char c = str2.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char)(c + 32); // Converte para minúscula
            }
            contador[c]--;
        }
        
        // Verificando se todos os contadores estão em zero
        for (int i = 0; i < contador.length; i++) {
            if (contador[i] != 0) {
                return false;
            }
        }
        
        return true;
    }

    public static boolean stop(String input) {
        return input.equals("FIM");
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String linha = "";
        
        do {
            linha = scanner.nextLine();
            
            if (!stop(linha)) {
                // Encontrar o delimitador e separar as strings manualmente
                String str1 = "";
                String str2 = "";
                boolean encontrouDelimitador = false;
                
                for (int i = 0; i < linha.length(); i++) {
                    char c = linha.charAt(i);
                    
                    // Verificar se estamos no delimitador " - "
                    if (!encontrouDelimitador && c == ' ' && i + 2 < linha.length() && 
                        linha.charAt(i + 1) == '-' && linha.charAt(i + 2) == ' ') {
                        encontrouDelimitador = true;
                        i += 2; // Pular " - "
                        continue;
                    }
                    
                    // Adicionar caractere à string apropriada
                    if (!encontrouDelimitador) {
                        str1 += c;
                    } else {
                        str2 += c;
                    }
                }
                
                if (encontrouDelimitador) {
                    if (saoAnagramas(str1, str2)) {
                        System.out.println("SIM");
                    } else {
                        System.out.println("NÃO");
                    }
                }
            }
        } while (!stop(linha));
        
        scanner.close();
    }
}
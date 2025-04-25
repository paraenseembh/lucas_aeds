import java.util.HashMap;
import java.util.Scanner;

public class Q11 {
    
    /**
     * Método iterativo que encontra o comprimento da substring mais longa sem caracteres repetidos
     * @param s string de entrada
     * @return comprimento da substring mais longa sem caracteres repetidos
     */
    public static int lengthOfLongestSubstring(String s) {
        // Caso a string seja vazia, retorna 0
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // Caso a string tenha apenas um caractere, retorna 1
        if (s.length() == 1) {
            return 1;
        }
        
        // HashMap para armazenar o último índice de cada caractere
        HashMap<Character, Integer> charMap = new HashMap<>();
        
        int maxLength = 0; // Comprimento máximo da substring
        int start = 0;     // Início da janela atual
        
        // Percorre a string
        for (int end = 0; end < s.length(); end++) {
            char currentChar = s.charAt(end);
            
            // Se o caractere atual já foi encontrado e está dentro da janela atual
            if (charMap.containsKey(currentChar) && charMap.get(currentChar) >= start) {
                // Move o início da janela para depois da última ocorrência do caractere
                start = charMap.get(currentChar) + 1;
            }
            
            // Atualiza o último índice do caractere atual
            charMap.put(currentChar, end);
            
            // Atualiza o comprimento máximo
            maxLength = Math.max(maxLength, end - start + 1);
        }
        
        return maxLength;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Processa cada linha de entrada
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            int result = lengthOfLongestSubstring(input);
            System.out.println(result);
        }
        
        scanner.close();
    }
}
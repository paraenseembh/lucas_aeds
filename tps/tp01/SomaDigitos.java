public class SomaDigitos {
    
    public static int somaDigitosRecursivo(int numero) {
        // Caso base: se o número for menor que 10, retorna o próprio número
        if (numero < 10) {
            return numero;
        }
        
        // Caso recursivo: soma o último dígito com a soma dos dígitos restantes
        return (numero % 10) + somaDigitosRecursivo(numero / 10);
    }
    
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        while (scanner.hasNextInt()) {
            int numero = scanner.nextInt();
            int resultado = somaDigitosRecursivo(numero);
            System.out.println(resultado);
        }
        
        scanner.close();
    }
} 
    

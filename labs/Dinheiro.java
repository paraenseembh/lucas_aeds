/*In this problem you have to read an integer value and calculate the smallest possible number of banknotes in which the value may be decomposed. 
The possible banknotes are 100, 50, 20, 10, 5, 2 and 1. Print the read value and the list of banknotes.

Input
The input file contains an integer value N (0 < N < 1000000).

Output
Print the read number and the minimum quantity of each necessary banknotes in Portuguese language, as the given example.
Do not forget to print the end of line after each line, otherwise you will receive “Presentation Error”.*/


public class Main {
 
    public static void main(String[] args) throws IOException {
        int cem = 0; 
        int cinquenta = 0;
        int vinte = 0;
        int dez = 0; 
        int cinco = 0;
        int dois = 0;
        int um = 0;
        /**
         * Escreva a sua solução aqui
         * Code your solution here
         * Escriba su solución aquí
         */
        Scanner scanner = new Scanner(System.in);
        int valor = scanner.nextInt();
        
        
        cem = valor % 100; 
        cinquenta = (valor - Math.round((valor/100)))%50 
        System.out.println(cem + "nota(s) de R%100,00")
        cinquenta = (valor = cem*100)%50
    }
    
    public divide_notas(int valor){
        return divide_notas()
    }
 

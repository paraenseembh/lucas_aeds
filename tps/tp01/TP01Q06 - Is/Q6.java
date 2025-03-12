

public class Q6 {

    public String retornaMinusculo(String input){
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

    public boolean somenteVogais (String input){
        boolean resultado = false;
        for (int i = 0; i < input.length(); i++){
            if (input.charAt(i) == 'a' || input.charAt(i) == 'e' || input.charAt(i) == 'i' || input.charAt(i) == 'o' || input.charAt(i) == 'u'){
                resultado = true;
            } else {
                resultado = false;
            }
        }
        return resultado;
    }
    
    public static boolean somenteConsoantes (String input){
        boolean resultado = false;
        for (int i = 0; i < input.length(); i++){
            if (input.charAt(i) != 'a' || input.charAt(i) != 'e' || input.charAt(i) != 'i' || input.charAt(i) != 'o' || input.charAt(i) != 'u'){
                resultado = true;
            } else {
                resultado = false;
            }
        }
        return resultado;
    
    }

    public static void main(String []args){

        
    }

}




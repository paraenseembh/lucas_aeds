public class VogaisConsoantes {

    // 1. Mudamos para 'boolean' (true/false real) e 'static' para rodar no main
    private static boolean somenteVogais(String s) {
        // 2. Erro corrigido: 'length()' com 'th' e parênteses. O tipo deve ser int.
        int n = s.length(); 
        
        // 3. Transformamos em minúsculo para não precisar testar 'A', 'E', etc.
        s = s.toLowerCase();

        for (int i = 0; i < n; i++) {
            char caractere = s.charAt(i); // 4. Erro corrigido: charAt é um método (), não array []

            // Se encontrarmos QUALQUER coisa que NÃO seja vogal, já retornamos false
            if (!(caractere == 'a' || caractere == 'e' || caractere == 'i' || caractere == 'o' || caractere == 'u')) {
                return false; 
            }
        }

        // Se o loop terminar sem retornar false, significa que tudo é vogal
        return true;
    }

    public static void main(String[] args) {
        String teste = "aeiou";
        if (somenteVogais(teste)) {
            System.out.println("A string contém apenas vogais!");
        } else {
            System.out.println("A string contém consoantes ou outros caracteres.");
        }
    }
}

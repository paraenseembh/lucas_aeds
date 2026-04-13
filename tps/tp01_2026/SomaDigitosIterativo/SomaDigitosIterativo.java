public class SomaDigitosIterativo {

    static boolean ehFIM(String s) {
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    static int somaDigitos(String s) {
        int soma = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') soma += c - '0';
        }
        return soma;
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFIM(linha)) {
            MyIO.println(somaDigitos(linha));
            linha = MyIO.readLine();
        }
    }
}

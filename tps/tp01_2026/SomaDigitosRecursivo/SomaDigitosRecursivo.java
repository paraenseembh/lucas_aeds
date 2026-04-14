public class SomaDigitosRecursivo {

    static int somaDigitos(String s, int i) {
        if (i == s.length()) return 0;
        char c = s.charAt(i);
        int digito = (c >= '0' && c <= '9') ? c - '0' : 0;
        return digito + somaDigitos(s, i + 1);
    }

    static boolean ehFIM(String s) {
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFIM(linha)) {
            MyIO.println(somaDigitos(linha, 0));
            linha = MyIO.readLine();
        }
    }
}

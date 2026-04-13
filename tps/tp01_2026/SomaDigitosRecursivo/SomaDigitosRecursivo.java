public class SomaDigitosRecursivo {

    static int somaDigitos(String s, int i) {
        if (i == s.length()) return 0;
        char c = s.charAt(i);
        int digito = (c >= '0' && c <= '9') ? c - '0' : 0;
        return digito + somaDigitos(s, i + 1);
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(somaDigitos(linha, 0));
            linha = MyIO.readLine();
        }
    }
}

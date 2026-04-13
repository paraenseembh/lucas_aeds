public class VerificacaoAnagramaIterativo {

    static boolean saoAnagramas(String s1, String s2) {
        if (s1.length() != s2.length()) return false;

        int[] contador = new int[65536];

        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            if (c >= 'A' && c <= 'Z') c = (char)(c + 32);
            contador[c]++;
        }

        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (c >= 'A' && c <= 'Z') c = (char)(c + 32);
            contador[c]--;
        }

        for (int i = 0; i < contador.length; i++) {
            if (contador[i] != 0) return false;
        }

        return true;
    }

    static boolean ehFim(String s) {
        return s.equals("FIM");
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();

        while (!ehFim(linha)) {
            int espaco = linha.indexOf(' ');
            if (espaco != -1) {
                String s1 = linha.substring(0, espaco);
                String s2 = linha.substring(espaco + 1);
                MyIO.println(saoAnagramas(s1, s2) ? "SIM" : "NAO");
            }

            linha = MyIO.readLine();
        }
    }
}

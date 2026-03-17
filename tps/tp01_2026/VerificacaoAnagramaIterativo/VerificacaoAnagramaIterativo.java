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
            String s1 = "", s2 = "";
            boolean achouDelimitador = false;

            for (int i = 0; i < linha.length(); i++) {
                char c = linha.charAt(i);

                if (!achouDelimitador && c == ' ' && i + 2 < linha.length()
                        && linha.charAt(i + 1) == '-' && linha.charAt(i + 2) == ' ') {
                    achouDelimitador = true;
                    i += 2;
                    continue;
                }

                if (!achouDelimitador) s1 += c;
                else s2 += c;
            }

            if (achouDelimitador) {
                MyIO.println(saoAnagramas(s1, s2) ? "SIM" : "NÃO");
            }

            linha = MyIO.readLine();
        }
    }
}

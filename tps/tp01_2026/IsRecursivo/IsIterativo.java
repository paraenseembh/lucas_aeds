public class IsIterativo {

    static boolean ehFIM(String s) {
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    static boolean ehVogal(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    static boolean ehLetraASCII(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    static char toLower(char c) {
        return (c >= 'A' && c <= 'Z') ? (char)(c + 32) : c;
    }

    static boolean somenteVogais(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = toLower(s.charAt(i));
            if (!ehLetraASCII(c) || !ehVogal(c)) return false;
        }
        return true;
    }

    static boolean somenteConsoantes(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = toLower(s.charAt(i));
            if (!ehLetraASCII(c) || ehVogal(c)) return false;
        }
        return true;
    }

    static boolean ehInt(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    static boolean ehReal(String s) {
        if (s.isEmpty()) return false;
        boolean temPonto = false, temDigito = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                temDigito = true;
            } else if (c == '.') {
                if (temPonto) return false;
                temPonto = true;
            } else {
                return false;
            }
        }
        return temPonto && temDigito;
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFIM(linha)) {
            MyIO.println(
                (somenteVogais(linha) ? "SIM" : "NAO") + " " +
                (somenteConsoantes(linha) ? "SIM" : "NAO") + " " +
                (ehInt(linha) ? "SIM" : "NAO") + " " +
                (ehReal(linha) ? "SIM" : "NAO")
            );
            linha = MyIO.readLine();
        }
    }
}

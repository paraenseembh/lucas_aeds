public class IsRecursivo {

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

    static boolean somenteVogais(String s, int i) {
        if (i == s.length()) return i > 0;
        char c = toLower(s.charAt(i));
        if (!ehLetraASCII(c) || !ehVogal(c)) return false;
        return somenteVogais(s, i + 1);
    }

    static boolean somenteConsoantes(String s, int i) {
        if (i == s.length()) return i > 0;
        char c = toLower(s.charAt(i));
        if (!ehLetraASCII(c) || ehVogal(c)) return false;
        return somenteConsoantes(s, i + 1);
    }

    static boolean ehInt(String s, int i) {
        if (i == s.length()) return i > 0;
        char c = s.charAt(i);
        if (c < '0' || c > '9') return false;
        return ehInt(s, i + 1);
    }

    static boolean ehReal(String s, int i, boolean temPonto, boolean temDigito) {
        if (i == s.length()) return temPonto && temDigito;
        char c = s.charAt(i);
        if (c >= '0' && c <= '9') return ehReal(s, i + 1, temPonto, true);
        if (c == '.' && !temPonto) return ehReal(s, i + 1, true, temDigito);
        return false;
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFIM(linha)) {
            MyIO.println(
                (somenteVogais(linha, 0) ? "SIM" : "NAO") + " " +
                (somenteConsoantes(linha, 0) ? "SIM" : "NAO") + " " +
                (ehInt(linha, 0) ? "SIM" : "NAO") + " " +
                (ehReal(linha, 0, false, false) ? "SIM" : "NAO")
            );
            linha = MyIO.readLine();
        }
    }
}

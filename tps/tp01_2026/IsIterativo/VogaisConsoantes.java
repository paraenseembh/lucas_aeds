public class VogaisConsoantes {

    static boolean ehVogal(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    static boolean ehLetraASCII(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    static char toLower(char c) {
        return (c >= 'A' && c <= 'Z') ? (char)(c + 32) : c;
    }

    static String somenteVogais(String s) {
        if (s.isEmpty()) return "NAO";
        for (int i = 0; i < s.length(); i++) {
            char c = toLower(s.charAt(i));
            if (!ehLetraASCII(c) || !ehVogal(c)) return "NAO";
        }
        return "SIM";
    }

    static String somenteConsoantes(String s) {
        if (s.isEmpty()) return "NAO";
        for (int i = 0; i < s.length(); i++) {
            char c = toLower(s.charAt(i));
            if (!ehLetraASCII(c) || ehVogal(c)) return "NAO";
        }
        return "SIM";
    }

    static String ehInt(String s) {
        if (s.isEmpty()) return "NAO";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return "NAO";
        }
        return "SIM";
    }

    static String ehReal(String s) {
        if (s.isEmpty()) return "NAO";
        boolean temPonto = false, temDigito = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                temDigito = true;
            } else if (c == '.') {
                if (temPonto) return "NAO";
                temPonto = true;
            } else {
                return "NAO";
            }
        }
        return (temDigito && temPonto) ? "SIM" : "NAO";
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(somenteVogais(linha) + " " + somenteConsoantes(linha)
                       + " " + ehInt(linha) + " " + ehReal(linha));
            linha = MyIO.readLine();
        }
    }
}

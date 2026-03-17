public class VogaisConsoantes {

    static boolean ehVogal(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    static String somenteVogais(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = (char)(s.charAt(i) + (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z' ? 32 : 0));
            if (c < 'a' || c > 'z' || !ehVogal(c)) return "NAO";
        }
        return "SIM";
    }

    static String somenteConsoantes(String s) {
        if (s.isEmpty()) return "NAO";
        for (int i = 0; i < s.length(); i++) {
            char c = (char)(s.charAt(i) + (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z' ? 32 : 0));
            if (c < 'a' || c > 'z' || ehVogal(c)) return "NAO";
        }
        return "SIM";
    }

    static String ehInt(String s) {
        if (s.isEmpty()) return "NAO";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') return "NAO";
        }
        return "SIM";
    }

    static String ehReal(String s) {
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

    static boolean ehFim(String s) {
        return s.equals("FIM");
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFim(linha)) {
            MyIO.println(somenteVogais(linha) + " " + somenteConsoantes(linha) + " " + ehInt(linha) + " " + ehReal(linha));
            linha = MyIO.readLine();
        }
    }
}

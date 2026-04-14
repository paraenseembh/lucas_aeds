public class ValidacaoSenhaRecursivo {

    static boolean ehFIM(String s) {
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    static boolean senhaValida(String s, int i, boolean mai, boolean min, boolean dig, boolean esp) {
        if (i == s.length()) return mai && min && dig && esp;
        char c = s.charAt(i);
        if (c >= 'A' && c <= 'Z') mai = true;
        else if (c >= 'a' && c <= 'z') min = true;
        else if (c >= '0' && c <= '9') dig = true;
        else esp = true;
        return senhaValida(s, i + 1, mai, min, dig, esp);
    }

    static boolean senhaValida(String s) {
        if (s.length() < 8) return false;
        return senhaValida(s, 0, false, false, false, false);
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFIM(linha)) {
            MyIO.println(senhaValida(linha) ? "SIM" : "NAO");
            linha = MyIO.readLine();
        }
    }
}

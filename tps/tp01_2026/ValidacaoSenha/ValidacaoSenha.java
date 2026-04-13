public class ValidacaoSenha {

    static boolean senhaValida(String s) {
        if (s.length() < 8) return false;
        boolean maiuscula = false, minuscula = false, digito = false, especial = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'A' && c <= 'Z') maiuscula = true;
            else if (c >= 'a' && c <= 'z') minuscula = true;
            else if (c >= '0' && c <= '9') digito = true;
            else especial = true;
        }
        return maiuscula && minuscula && digito && especial;
    }

    static boolean ehFIM(String s) {
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFIM(linha)) {
            MyIO.println(senhaValida(linha) ? "SIM" : "NAO");
            linha = MyIO.readLine();
        }
    }
}

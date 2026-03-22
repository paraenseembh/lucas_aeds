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

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(senhaValida(linha) ? "SIM" : "NÃO");
            linha = MyIO.readLine();
        }
    }
}

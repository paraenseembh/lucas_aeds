import java.nio.charset.Charset;

class CiframentoCesarRecursivo {

    static Charset CP1252 = Charset.forName("windows-1252");

    static char cifrarChar(char c) {
        byte[] bytes = String.valueOf(c).getBytes(CP1252);
        if (bytes.length == 1) {
            byte shifted = (byte)((bytes[0] & 0xFF) + 3);
            return new String(new byte[]{shifted}, CP1252).charAt(0);
        }
        return (char)(c + 3);
    }

    static String cifrar(String s, int i) {
        if (i == s.length()) return "";
        return cifrarChar(s.charAt(i)) + cifrar(s, i + 1);
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        String linha = MyIO.readLine();
        while (linha != null && !linha.equals("FIM")) {
            MyIO.println(cifrar(linha, 0));
            linha = MyIO.readLine();
        }
    }
}

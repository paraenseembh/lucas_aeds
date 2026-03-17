import java.io.*;
import java.nio.charset.Charset;

class CiframentoCesar {

    static Charset CP1252 = Charset.forName("windows-1252");

    static char cifrarChar(char c) {
        byte[] bytes = String.valueOf(c).getBytes(CP1252);
        if (bytes.length == 1) {
            byte shifted = (byte)((bytes[0] & 0xFF) + 3);
            return new String(new byte[]{shifted}, CP1252).charAt(0);
        }
        return (char)(c + 3);
    }

    static String cifrar(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            sb.append(cifrarChar(s.charAt(i)));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        String linha = br.readLine();
        while (linha != null && !linha.equals("FIM")) {
            out.println(cifrar(linha));
            linha = br.readLine();
        }
    }
}

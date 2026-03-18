import java.io.*;

public class InversaoStringRecursivo {

    static String inverter(String s) {
        if (s.length() <= 1) return s;
        return inverter(s.substring(1)) + s.charAt(0);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        String linha = br.readLine();
        while (linha != null && !linha.equals("FIM")) {
            out.println(inverter(linha));
            linha = br.readLine();
        }
    }
}

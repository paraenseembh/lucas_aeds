public class InversaoStringRecursivo {

    static String inverter(String s) {
        if (s.length() <= 1) return s;
        return inverter(s.substring(1)) + s.charAt(0);
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (linha != null && !linha.equals("FIM")) {
            MyIO.println(inverter(linha));
            linha = MyIO.readLine();
        }
    }
}

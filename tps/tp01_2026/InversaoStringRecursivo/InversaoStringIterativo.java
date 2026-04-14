public class InversaoStringIterativo {

    static boolean ehFIM(String s) {
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    static String inverter(String s) {
        String resultado = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            resultado += s.charAt(i);
        }
        return resultado;
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFIM(linha)) {
            MyIO.println(inverter(linha));
            linha = MyIO.readLine();
        }
    }
}

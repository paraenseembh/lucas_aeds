public class SubstringMaisLonga {

    static int maiorSubstring(String s) {
        int[] ultimo = new int[65536];
        for (int i = 0; i < ultimo.length; i++) ultimo[i] = -1;
        int max = 0, inicio = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (ultimo[c] >= inicio) inicio = ultimo[c] + 1;
            ultimo[c] = i;
            if (i - inicio + 1 > max) max = i - inicio + 1;
        }
        return max;
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(maiorSubstring(linha));
            linha = MyIO.readLine();
        }
    }
}

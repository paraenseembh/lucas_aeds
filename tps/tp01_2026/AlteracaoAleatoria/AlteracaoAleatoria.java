import java.util.Random;

class AlteracaoAleatoria {

    static char sorteiaLetra(Random r) {
        return (char)('a' + r.nextInt(26));
    }

    static String alterar(String s, char de, char para) {
        String resultado = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            resultado += (c == de) ? para : c;
        }
        return resultado;
    }

    public static void main(String[] args) {
        Random r = new Random();
        r.setSeed(4);
        char letra1 = sorteiaLetra(r);
        char letra2 = sorteiaLetra(r);

        String linha = MyIO.readLine();
        while (linha != null && !linha.equals("FIM")) {
            MyIO.println(alterar(linha, letra1, letra2));
            linha = MyIO.readLine();
        }
    }
}

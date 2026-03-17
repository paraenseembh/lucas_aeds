class CiframentoCesar {

    static boolean ehFim(String s) {
        return s.equals("FIM");
    }

    static String cifrar(String s) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char)(chars[i] + 3);
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        String linha = MyIO.readLine();
        while (!ehFim(linha)) {
            MyIO.println(cifrar(linha));
            linha = MyIO.readLine();
        }
    }
}

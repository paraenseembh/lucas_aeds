import java.util.Random;


public class AlteracaoAleatoria {

char c = 'b';

public void EhConsoante (char c) {
c = Character.toLowerCase(c); // Normaliza para minúsculo

if (Character.isLetter(c) && !(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')) {
    System.out.println(c + " é uma consoante.");
} else {
    System.out.println(c + " não é uma consoante.");
}

}
public static void main (String[] args){

Random gerador = new Random( ) ;
gerador.setSeed(4);
System.out.println((char)('a' + (Math.abs(gerador.nextInt()) % 26)));

}


}



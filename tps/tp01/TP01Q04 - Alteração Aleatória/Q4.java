import java.util.*;

/*- Crie um método iterativo que recebe uma string, sorteia duas letras
minúsculas aleatórias (código ASCII ≥ ’a’ e ≤ ’z’), substitui todas as ocorrências da primeira
letra na string pela segunda e retorna a string com as alterações efetuadas. 

Na saı́da padrão,
para cada linha de entrada, execute o método desenvolvido nesta questão e mostre a string
retornada como uma linha de saı́da. Abaixo, observamos um exemplo de entrada supondo que
para a primeira linha as letras sorteados foram o ’a’ e o ’q’. Para a segunda linha, foram o ’e’ e
o ’k’. */
public class Q4 {
    public static char sorteiaLetra(Random random){
        return (char) ('a' + random.nextInt(26));
    }
    public static String alteraLetras(String input){
        
        Random random = new Random();
        random.setSeed(4);
        char letra1 = sorteiaLetra(random);
        char letra2 = sorteiaLetra(random);

        int n = input.length();
        char ArrayPalavra[] = new char[1000];
        String resultado = "";
        for(int i = 0; i < n; i++){
            ArrayPalavra[i] = input.charAt(i);
            if(ArrayPalavra[i] == letra1){
                ArrayPalavra[i] = letra2;
            }
            resultado += ArrayPalavra[i];
        }
        return resultado;
        }

        
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while(!input.equals("FIM")){
            System.out.println(alteraLetras(input));
            input = scanner.nextLine();
        }
        scanner.close();

        }
    }


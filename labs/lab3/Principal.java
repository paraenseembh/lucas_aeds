class Principal {
    public static void main(String[] args) {

        // Declaração de variáveis
        // Geracao algoritmo; // Nota: Certifique-se que a classe Geracao existe no seu projeto
        int n = MyIO.readInt(); 
        int m = MyIO.readInt();
        
        // CORREÇÃO: Sintaxe do array corrigida para int[]
        int[] array = new int[n]; 
        
        for(int i = 0; i < n ; i ++){
            // CORREÇÃO: Variável 'y' precisa ser declarada (int)
            int y = MyIO.readInt();
            if (y == 0) {break;}
            array[i] = y % m;
        }
      
        // Algoritmo de Selection Sort
        for (int i = 0; i < (n - 1); i++) {
            int menor = i;
            for (int j = (i + 1); j < n; j++){
                if (array[menor] > array[j]){
                    menor = j;
                }
            }
            
            // CORREÇÃO: A troca (swap) deve ser feita nos VALORES do array
            int temp = array[menor];
            array[menor] = array[i];
            array[i] = temp;
        }
        MyIO.println(n + " " + m);
        for(int i = 0; i < n; i++){
        MyIO.println(array[i]);
        }
        MyIO.println("0 0");
    }
}

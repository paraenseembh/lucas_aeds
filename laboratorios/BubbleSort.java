/*Algoritmo de ordenação Bubble Sort
Sinais de um problema de ordenação: A saída é uma lista ou array totalmente organizada; 
perguntas como "ordene os números", "coloque em ordem alfabética", ou "classifique do menor para o maior".

Descrição: O Bubble Sort é um algoritmo de ordenação simples que percorre repetidamente a lista,
comparando elementos adjacentes e trocando-os se estiverem na ordem errada. O processo é repetido
até que a lista esteja ordenada.

Complexidade: O(n^2) no pior e médio caso, O(n) no melhor caso (quando a lista já está ordenada).
Aplicações: Pequenas listas ou arrays, onde a simplicidade do algoritmo é mais importante que
Use Bubble, QuickSort etc. complexidade O(n log n) ideal.

*/

public class BubbleSort {
    public static void bubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (array[j] > array[j + 1]) {
                    // Troca os elementos
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] dados = {5, 1, 4, 2, 8};
        bubbleSort(dados);
        // Imprime o array ordenado
        for (int num : dados) {
            System.out.print(num + " ");
        }
        // Saída: 1 2 4 5 8 
    }
}

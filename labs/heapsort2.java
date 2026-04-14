public class HeapSort {

    public void heapSort(int[] array) {
        int n = array.length;

        // 1. Constrói o Max-Heap (reorganiza o array)
        montaMaxHeap(array);

        // 2. Extrai um a um os elementos do heap
        for (int i = n - 1; i > 0; i--) {
            // Move a raiz atual (maior elemento) para o fim do array
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // Chama o heapify no heap reduzido
            heapify(0, i, array);
        }
    }

    public void montaMaxHeap(int[] array) {
        int n = array.length;
        // Começa do último nó que possui filhos e vai subindo
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(i, n, array);
        }
    }

    public void heapify(int i, int tamHeap, int[] array) {
        int maior = i;
        int esq = 2 * i + 1;
        int dir = 2 * i + 2;

        // Verifica se o filho da esquerda é maior que a raiz
        if (esq < tamHeap && array[esq] > array[maior]) {
            maior = esq;
        }

        // Verifica se o filho da direita é maior que o maior atual
        if (dir < tamHeap && array[dir] > array[maior]) {
            maior = dir;
        }

        // Se o maior não for a raiz, realiza a troca e continua o processo
        if (maior != i) {
            int temp = array[i];
            array[i] = array[maior];
            array[maior] = temp;

            // Chama recursivamente para o nó afetado
            heapify(maior, tamHeap, array);
        }
    }
}

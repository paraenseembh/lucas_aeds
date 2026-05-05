public class Culinaria {

    private static CelulaPais primeiroPais;

    // Estrutura 1: Lista Encadeada de Países
    class CelulaPais {
        String pais;
        No raizEstado; // Raiz da árvore de estados
        CelulaPais prox;

        public CelulaPais(String pais) {
            this.pais = pais;
            this.raizEstado = null;
            this.prox = null;
        }
    }

    // Estrutura 3: Lista Encadeada de Comidas
    class CelulaComida {
        String comida;
        CelulaComida prox;

        public CelulaComida(String comida) {
            this.comida = comida;
            this.prox = null;
        }
    }

    // Estrutura 2: Árvore Binária de Estados
    class No {
        String estado;
        CelulaComida primeiraComida;
        No esq, dir;

        public No(String estado) {
            this.estado = estado;
            this.primeiraComida = null;
            this.esq = this.dir = null;
        }
    }

    // Métodos de Inserção na Árvore (BST)
    private No inserirEstado(No i, String estado, String comida) {
        if (i == null) {
            No novo = new No(estado);
            novo.primeiraComida = inserirComida(novo.primeiraComida, comida);
            return novo;
        }

        if (estado.compareTo(i.estado) < 0) {
            i.esq = inserirEstado(i.esq, estado, comida);
        } else if (estado.compareTo(i.estado) > 0) {
            i.dir = inserirEstado(i.dir, estado, comida);
        } else {
            // Estado já existe, apenas adiciona a comida na lista dele
            i.primeiraComida = inserirComida(i.primeiraComida, comida);
        }
        return i;
    }

    // Método de Inserção na Lista de Comidas
    private CelulaComida inserirComida(CelulaComida inicio, String nomeComida) {
        CelulaComida nova = new CelulaComida(nomeComida);
        if (inicio == null) return nova;
        
        CelulaComida atual = inicio;
        while (atual.prox != null) {
            if (atual.comida.equals(nomeComida)) return inicio; // Evita duplicatas
            atual = atual.prox;
        }
        if (!atual.comida.equals(nomeComida)) atual.prox = nova;
        return inicio;
    }

    public void inserir(String entrada) {
        String[] dados = entrada.split("/");
        if (dados.length < 3) return;

        String nomePais = dados[0];
        String nomeEstado = dados[1];
        String nomeComida = dados[2];

        // 1. Localizar ou Criar o País na Lista
        if (primeiroPais == null) {
            primeiroPais = new CelulaPais(nomePais);
        }

        CelulaPais atual = primeiroPais;
        CelulaPais anterior = null;

        while (atual != null && !atual.pais.equals(nomePais)) {
            anterior = atual;
            atual = atual.prox;
        }

        if (atual == null) { // País não encontrado, cria novo e add no fim
            atual = new CelulaPais(nomePais);
            anterior.prox = atual;
        }

        // 2. Inserir Estado e Comida na Árvore do País encontrado
        atual.raizEstado = inserirEstado(atual.raizEstado, nomeEstado, nomeComida);
    }

    public static void main(String[] args) {
        Culinaria sistema = new Culinaria();
        
        // Exemplo de entradas
        sistema.inserir("Brasil/MG/Pao de Queijo");
        sistema.inserir("Brasil/MG/Feijao Tropeiro");
        sistema.inserir("Brasil/SP/Virado a Paulista");
        sistema.inserir("EUA/Texas/BBQ");
        
        System.out.println("Dados inseridos com sucesso.");
    }
}
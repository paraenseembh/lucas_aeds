public class ArvoreBicolor {

    // Convenção: cor=false → sem cor (BRANCO), cor=true → colorido (PRETO)
    // Equivalência: BRANCO = nó raiz de cluster 2-3-4 (BLACK no rubro-negro clássico)
    //               PRETO  = nó extra dentro do cluster (RED no rubro-negro clássico)

    static class No {
        int chave;
        boolean cor; // false=sem cor (branco), true=colorido (preto)
        No esq, dir;

        No(int chave) {
            this.chave = chave;
            this.cor = true; // nós novos sempre inseridos como coloridos
        }

        // Retorna true se este nó é um 4-nó (ambos os filhos são coloridos)
        public boolean isNoTipo4() {
            return esq != null && dir != null && esq.cor && dir.cor;
        }
    }

    private No raiz;

    // Recoloração equivalente à fragmentação na Árvore 2-3-4
    // i fica colorido, seus filhos ficam sem cor
    // Exceção: se i é a raiz global, permanece sem cor
    private void fragmentar(No i) {
        i.cor = true;
        if (i.esq != null) i.esq.cor = false;
        if (i.dir != null) i.dir.cor = false;
        if (i == raiz) i.cor = false;
    }

    // Rotação simples à esquerda (sobe o filho direito)
    private No rotacaoSimplesEsquerda(No avo) {
        No pai = avo.dir;
        avo.dir = pai.esq;
        pai.esq = avo;
        return pai;
    }

    // Rotação simples à direita (sobe o filho esquerdo)
    private No rotacaoSimplesDireita(No avo) {
        No pai = avo.esq;
        avo.esq = pai.dir;
        pai.dir = avo;
        return pai;
    }

    // Rotação dupla direita-esquerda (pai à direita de avô, nó à esquerda de pai)
    private No rotacaoDuplaDireitaEsquerda(No avo) {
        avo.dir = rotacaoSimplesDireita(avo.dir);
        return rotacaoSimplesEsquerda(avo);
    }

    // Rotação dupla esquerda-direita (pai à esquerda de avô, nó à direita de pai)
    private No rotacaoDuplaEsquerdaDireita(No avo) {
        avo.esq = rotacaoSimplesEsquerda(avo.esq);
        return rotacaoSimplesDireita(avo);
    }

    // Balanceamento por rotação envolvendo avô, pai e nó i
    // Ao final: nova raiz do triplo é sem cor, seus filhos são coloridos
    private void balancear(No bisavo, No avo, No pai, No i) {
        No novaRaiz;

        if (pai.chave > avo.chave) {
            // pai é filho direito do avô
            if (i.chave > pai.chave) {
                novaRaiz = rotacaoSimplesEsquerda(avo);   // alinhamento direita-direita
            } else {
                novaRaiz = rotacaoDuplaDireitaEsquerda(avo); // alinhamento direita-esquerda
            }
        } else {
            // pai é filho esquerdo do avô
            if (i.chave < pai.chave) {
                novaRaiz = rotacaoSimplesDireita(avo);    // alinhamento esquerda-esquerda
            } else {
                novaRaiz = rotacaoDuplaEsquerdaDireita(avo); // alinhamento esquerda-direita
            }
        }

        novaRaiz.cor = false;
        if (novaRaiz.esq != null) novaRaiz.esq.cor = true;
        if (novaRaiz.dir != null) novaRaiz.dir.cor = true;

        // Conecta novaRaiz ao bisavô
        if (bisavo == null) {
            raiz = novaRaiz;
        } else if (novaRaiz.chave < bisavo.chave) {
            bisavo.esq = novaRaiz;
        } else {
            bisavo.dir = novaRaiz;
        }
    }

    // Inserção pública: trata árvore vazia; senão chama inserção recursiva
    public void inserir(int x) {
        if (raiz == null) {
            raiz = new No(x);
            raiz.cor = false; // raiz sempre sem cor
        } else {
            inserir(x, null, null, null, raiz);
        }
    }

    // Inserção recursiva top-down com reequilíbrios
    // bisavo, avo, pai = ancestrais do nó atual i (podem ser null no início)
    private void inserir(int x, No bisavo, No avo, No pai, No i) {
        if (i == null) {
            // Posição de inserção encontrada: inserir como folha colorida
            No novo = new No(x);
            if (x < pai.chave) pai.esq = novo;
            else               pai.dir = novo;

            // Se o pai for colorido, há violação → balancear
            if (pai != null && pai.cor) {
                balancear(bisavo, avo, pai, novo);
            }
            return;
        }

        // Ponteiros padrão para a chamada recursiva seguinte
        No recBisavo = avo;
        No recAvo    = pai;

        // Verifica e trata 4-nós antes de descer
        if (i.isNoTipo4()) {
            fragmentar(i);

            // Após fragmentar, i fica colorido; se pai também for colorido → balancear
            if (pai != null && pai.cor) {
                // Determina se a rotação será simples (true) ou dupla (false)
                boolean rotacaoSimples = (pai.chave > avo.chave)
                        ? (i.chave > pai.chave)   // direita-direita → simples esquerda
                        : (i.chave < pai.chave);  // esquerda-esquerda → simples direita

                balancear(bisavo, avo, pai, i);

                if (rotacaoSimples) {
                    // novaRaiz = pai (objeto Java inalterado); i ainda é filho de pai
                    recBisavo = bisavo;
                    recAvo    = pai;
                } else {
                    // novaRaiz = i; i sobe e fica sem cor → próximo nível não precisará balancear
                    recBisavo = null;
                    recAvo    = bisavo;
                }
            }
        }

        if (x < i.chave) {
            inserir(x, recBisavo, recAvo, i, i.esq);
        } else if (x > i.chave) {
            inserir(x, recBisavo, recAvo, i, i.dir);
        }
        // x == i.chave: chave duplicada, ignorada
    }

    // Caminhamento central (in-order) imprimindo chave e cor
    public void caminhamentoCentral() {
        caminhamentoCentral(raiz);
        System.out.println();
    }

    private void caminhamentoCentral(No no) {
        if (no == null) return;
        caminhamentoCentral(no.esq);
        System.out.print(no.chave + "(cor=" + (no.cor ? 1 : 0) + ") ");
        caminhamentoCentral(no.dir);
    }

    // Impressão visual da árvore no terminal (rotacionada 90 graus, raiz à esquerda)
    // Leitura: direito = cima, esquerdo = baixo
    public void imprimir() {
        if (raiz == null) {
            System.out.println("(arvore vazia)");
            return;
        }
        imprimir(raiz, "", true);
    }

    private void imprimir(No no, String prefixo, boolean ultimoFilho) {
        if (no == null) return;

        String prefixoFilho = prefixo + (ultimoFilho ? "       " : "|      ");
        imprimir(no.dir, prefixoFilho, true);

        String conector = prefixo.isEmpty() ? "-- " : (ultimoFilho ? "+-- " : "+-- ");
        String cor = no.cor ? "[P]" : "[B]"; // [P]=preto/colorido  [B]=branco/sem cor
        System.out.println(prefixo + conector + cor + " " + no.chave);

        imprimir(no.esq, prefixoFilho, false);
    }

    // Programa de validação
    public static void main(String[] args) {
        ArvoreBicolor arvore = new ArvoreBicolor();

        int[] elementos = {4, 35, 10, 13, 3, 30, 15, 12, 7, 40, 20};
        System.out.print("Inserindo: ");
        for (int e : elementos) {
            System.out.print(e + " ");
            arvore.inserir(e);
        }
        System.out.println();

        System.out.println("\nArvore visual ([P]=preto/colorido  [B]=branco/sem cor):");
        arvore.imprimir();

        System.out.println("\nCaminhamento central:");
        arvore.caminhamentoCentral();

        System.out.println("Esperado:");
        System.out.println("3(cor=1) 4(cor=0) 7(cor=1) 10(cor=1) 12(cor=0) 13(cor=0) 15(cor=0) 20(cor=1) 30(cor=1) 35(cor=0) 40(cor=1) ");
    }
}

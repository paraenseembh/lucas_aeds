public class BinaryTreeMirror {
    
    /**
     * Função principal para verificar se a árvore 'a' é o espelho da árvore 'b'.
     */
    public boolean isMirror(Node a, Node b) {
        // 1. Se ambas são nulas, elas são espelhos (caso base)
        if (a == null && b == null) {
            return true;
        }

        // 2. Se apenas uma for nula, ou os valores forem diferentes, não são espelhos
        if (a == null || b == null || a.data != b.data) {
            return false;
        }

        // 3. Recursão: 
        // Lado esquerdo de 'a' deve ser espelho do lado direito de 'b'
        // Lado direito de 'a' deve ser espelho do lado esquerdo de 'b'
        return isMirror(a.left, b.right) && isMirror(a.right, b.left);
    }
}

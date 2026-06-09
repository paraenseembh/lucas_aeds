class hashEstatico {

    int m; //tamanho da tabela principal
    int r; //tamanho da área de reserva
    String[] tabela; 
    String[] reserva;
    int nr; //numero de elementos armazenados na reserva
    private class ExcDuplicado extends Exception{}
    private class ExcReservaDuplicado extends Exception{}

  public hashEstatico(int m, int r){
    this.m = m; 
    this.r = r;
    this.tabela = new String[m] ;
    this.reserva = new String[r] ;
    this.nr = 0; 
    
  }  

  private int hash(String chave){

    int soma = 0; 

    for(int i=0; i <  chave.length(); i++){
        soma+= (int) chave.charAt(i);
    }

    return soma % this.m;

  }


  private boolean posicaoLivre(int pos){
    return this.tabela[pos] == null;

  }

  public void inserir(String s) {
    
    int hash = this.hash(s);

    if (this.posicaoLivre(hash)) {
        this.tabela[hash] == s;
    }
    if (!this.posicaoLivre(s) && this.tabela[hash] == chave){
        throw new ExcDuplicado();
    } else {
        for(int i= 0; i < r; i++){
            if(this.reserva[i] == null) break;
            if(this.reserva[i] == s){
                throw new ExcReservaDuplicado;
            }
        }
    }

    public String pesquisar(String chave){ 

        int hash = this.hash(chave);
        int temp = this.tabela[hash]; 

    }

    //implementar remover



  }

    

}
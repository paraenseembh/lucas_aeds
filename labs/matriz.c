#include <stdlib.h>
#include <stdio.h>

struct Celula{
	struct Celula *e, *d, *c, *b;
	int elemento;
} typedef Celula;

struct Matriz{
	Celula *inicio;
	int linha;
	int coluna;
} typedef Matriz;

Celula *criaCelula(int elemento){
	Celula *novaCelula = (Celula*)malloc(sizeof(Celula));
	novaCelula->e = NULL;
	novaCelula->d = NULL;
	novaCelula->c = NULL;
	novaCelula->b = NULL;
	novaCelula->elemento=elemento;
	return novaCelula;
}

Matriz *criarMatriz(int linha, int coluna){
	Matriz *novaMatriz = (Matriz*)malloc(sizeof(Matriz));
	novaMatriz->inicio=NULL;
	novaMatriz->linha=linha;
	novaMatriz->coluna=coluna;
	return novaMatriz;
}

int main(){
int linha, coluna;

while(scanf("%d" "%d", &linha, &coluna) != EOF){
	printf("\nvem matriz ai:\n");
	Matriz *matriz = criarMatriz(linha, coluna); 
	Celula *linhaAnterior[100];

	for(int i = 0; i < linha; i++){
		Celula *tmp = NULL;

		for(int j = 0; j < coluna;j++){
		//a cada i j crio uma nova celula
		int elemento; 
		scanf("%d", &elemento);
		printf("%d ", elemento);
		Celula *nova = criaCelula(elemento);

		if(i ==0 && j ==0){
			matriz->inicio=nova;
		}
		
		if(tmp != NULL){
			tmp->d = nova;
			nova->e = tmp;
		} 

		if( i>0 ){
			linhaAnterior[j]->b = nova;
			nova->c=linhaAnterior[j];
		
		}
		

		tmp = nova; 
		linhaAnterior[j] = nova;
		}

		printf("\n");
	}

}

}

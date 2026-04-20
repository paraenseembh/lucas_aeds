#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <locale.h>

#define MATRICULA "797798"
#define MAX_TIPOS 5
#define MAX_TIPO_LEN 30
#define MAX_STR 100

typedef struct { int ano, mes, dia; } Data;
typedef struct { int hora, minuto; } Hora;
typedef struct {
    int id; char nome[MAX_STR]; char cidade[MAX_STR];
    int capacidade; double avaliacao;
    char tipos_cozinha[MAX_TIPOS][MAX_TIPO_LEN]; int num_tipos;
    int faixa_preco;
    Hora horario_abertura; Hora horario_fechamento;
    Data data_abertura; int aberto;
} Restaurante;

#define MAX_REST 600
typedef struct { int tamanho; Restaurante* restaurantes[MAX_REST]; } Colecao_Restaurantes;

Restaurante* parse_restaurante(char* s) {
    Restaurante* r=(Restaurante*)malloc(sizeof(Restaurante));
    char buf[1000]; strncpy(buf,s,999); buf[999]='\0';
    int len=strlen(buf);
    while(len>0&&(buf[len-1]=='\n'||buf[len-1]=='\r')) buf[--len]='\0';
    char* fields[11]; int nf=0;
    char* tok=strtok(buf,",");
    while(tok&&nf<11){fields[nf++]=tok;tok=strtok(NULL,",");}
    r->id=atoi(fields[0]);strncpy(r->nome,fields[1],MAX_STR-1);strncpy(r->cidade,fields[2],MAX_STR-1);
    r->capacidade=atoi(fields[3]);r->avaliacao=atof(fields[4]);
    char tb[200];strncpy(tb,fields[5],199);
    r->num_tipos=0;char* tipo=strtok(tb,";");
    while(tipo){strncpy(r->tipos_cozinha[r->num_tipos++],tipo,MAX_TIPO_LEN-1);tipo=strtok(NULL,";");}
    r->faixa_preco=strlen(fields[6]);
    sscanf(fields[7],"%d:%d-%d:%d",&r->horario_abertura.hora,&r->horario_abertura.minuto,
           &r->horario_fechamento.hora,&r->horario_fechamento.minuto);
    sscanf(fields[8],"%d-%d-%d",&r->data_abertura.ano,&r->data_abertura.mes,&r->data_abertura.dia);
    char ab[10];strncpy(ab,fields[9],9);ab[9]='\0';
    len=strlen(ab);while(len>0&&(ab[len-1]=='\n'||ab[len-1]=='\r'))ab[--len]='\0';
    r->aberto=(strcmp(ab,"true")==0)?1:0;
    return r;
}

void ler_csv_colecao(Colecao_Restaurantes* col, char* path) {
    FILE* f=fopen(path,"r");if(!f)return;
    char line[1000];fgets(line,sizeof(line),f);col->tamanho=0;
    while(fgets(line,sizeof(line),f))if(strlen(line)>1)col->restaurantes[col->tamanho++]=parse_restaurante(line);
    fclose(f);
}

long long comparacoes=0;

void selecao_nome(Restaurante** arr, int n) {
    for(int i=0;i<n-1;i++){
        int min=i;
        for(int j=i+1;j<n;j++) if(strcmp(arr[j]->nome,arr[min]->nome)<0) min=j;
        if(min!=i){Restaurante* tmp=arr[i];arr[i]=arr[min];arr[min]=tmp;}
    }
}

int binaria(Restaurante** arr, int n, char* nome) {
    int lo=0,hi=n-1;
    while(lo<=hi){
        int mid=(lo+hi)/2;
        comparacoes++;
        int cmp=strcmp(arr[mid]->nome,nome);
        if(cmp==0) return 1;
        else if(cmp<0) lo=mid+1;
        else hi=mid-1;
    }
    return 0;
}

int main(){
    setlocale(LC_NUMERIC,"C");
    Colecao_Restaurantes col; col.tamanho=0;
    ler_csv_colecao(&col,"/tmp/restaurantes.csv");
    Restaurante* arr[600]; int n=0;
    int id;
    while(scanf("%d",&id)==1&&id!=-1){
        for(int i=0;i<col.tamanho;i++)
            if(col.restaurantes[i]->id==id){arr[n++]=col.restaurantes[i];break;}
    }
    selecao_nome(arr,n);
    char nome[200];
    // consume rest of int line
    char lixo[10]; fgets(lixo,sizeof(lixo),stdin);

    clock_t ini=clock();
    while(fgets(nome,sizeof(nome),stdin)){
        int len=strlen(nome);
        while(len>0&&(nome[len-1]=='\n'||nome[len-1]=='\r'))nome[--len]='\0';
        if(strcmp(nome,"FIM")==0) break;
        if(len==0) continue;
        printf("%s\n",binaria(arr,n,nome)?"SIM":"NAO");
    }
    clock_t fim=clock();
    double tempo=(double)(fim-ini)/CLOCKS_PER_SEC*1000.0;

    FILE* log=fopen("matricula_binaria.txt","w");
    fprintf(log,"%s\t%lld\t%.2f\n",MATRICULA,comparacoes,tempo);
    fclose(log);
    return 0;
}
